package ru.practicum.intershop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.CartItemDto;
import ru.practicum.intershop.dto.ItemWithCartDto;
import ru.practicum.intershop.dto.ItemsWithCartDto;
import ru.practicum.intershop.entities.Cart;
import ru.practicum.intershop.entities.CartItem;
import ru.practicum.intershop.entities.Product;
import ru.practicum.intershop.repositories.CartItemsRepository;
import ru.practicum.intershop.repositories.CartRepository;
import ru.practicum.intershop.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShopfrontServiceDefault implements ShopfrontService {

    private final CartItemsRepository cartItemsRepository;
    private ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Autowired
    public ShopfrontServiceDefault(ProductRepository productRepository, CartRepository cartRepository, CartItemsRepository cartItemsRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    public Mono<ItemsWithCartDto> getShopfrontPageWithCart(Pageable pageable, long cartId, String search) {
        Flux<Product> products = productRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
        Mono<Cart> cart = cartRepository.findById(cartId).switchIfEmpty(cartRepository.save(new Cart())).cache();
        Flux<CartItem> cartItems = cartItemsRepository.findByUserId(cartId);
        Mono<Long> countProducts = this.productRepository.count();
        return Mono.zip(products.collectList(), cartItems.collectList(), countProducts, cart).flatMap(tuple -> {
            var items = mergeProductsWithCart(tuple.getT1(), tuple.getT2());
            var pageItems = new PageImpl<>(items, pageable, tuple.getT3());
            return Mono.just(ItemsWithCartDto.builder().cartId(tuple.getT4().getId()).products(pageItems).build());
        });
    }

    @Override
    public Mono<ItemWithCartDto> getItemWithSelectedCount(long productId, long cartId) {
        Mono<Product> product = productRepository.findById(productId);
        Flux<CartItem> cartItems = cartItemsRepository.findByUserId(cartId);
        Mono<Cart> cart = cartRepository.findById(cartId).switchIfEmpty(cartRepository.save(new Cart())).cache();
        return Mono.zip(product, cartItems.collectList(), cart).map((tuple) -> ItemWithCartDto.builder()
                        .cartId(tuple.getT3().getId())
                        .product(Optional.of(mergeProductWithCart(tuple.getT1(), tuple.getT2())))
                        .build()
                )
                .switchIfEmpty(Mono.empty());
    }

    private CartItemDto mergeProductWithCart(Product product, List<CartItem> cartItems) {
        var productsInCart = cartItems.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .toList();
        return CartItemDto.builder()
                .id(product.getId())
                .price(product.getPrice())
                .imgPath(product.getImgPath())
                .title(product.getTitle())
                .description(product.getDescription())
                .count(productsInCart.isEmpty() ? 0 : productsInCart.getFirst().getCount())
                .build();
    }

    private List<CartItemDto> mergeProductsWithCart(List<Product> products, List<CartItem> cartItems) {
        return products.stream()
                .map(product -> mergeProductWithCart(product, cartItems))
                .toList();
    }
}
