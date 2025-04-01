package ru.practicum.intershop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.practicum.intershop.dto.CartItemDto;
import ru.practicum.intershop.dto.ItemWithCartDto;
import ru.practicum.intershop.dto.ItemsWithCartDto;
import ru.practicum.intershop.entities.Cart;
import ru.practicum.intershop.entities.Product;
import ru.practicum.intershop.repositories.CartRepository;
import ru.practicum.intershop.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopfrontServiceDefault implements ShopfrontService {

    private ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Autowired
    public ShopfrontServiceDefault(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public ItemsWithCartDto getShopfrontPageWithCart(Pageable pageable, long cardId, String search) {
        Page<Product> products = productRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search, pageable);
        Cart cart = cartRepository.findById(cardId).orElse(cartRepository.save(new Cart()));
        var items = mergeProductsWithCart(products.getContent(), cart);
        var pageItems =  new PageImpl<>(items, pageable, products.getTotalElements());
        return ItemsWithCartDto.builder().cartId(cart.getId()).products(pageItems).build();
    }

    @Override
    public ItemWithCartDto getItemWithSelectedCount(long productId, long cardId) {
        Optional<Product> product = productRepository.findById(productId);
        ItemWithCartDto item = ItemWithCartDto.builder().product(Optional.empty()).cartId(cardId).build();
        if (product.isPresent()) {
            Cart cart = cartRepository.findById(cardId).orElse(cartRepository.save(new Cart()));
            item.setProduct(Optional.of(mergeProductWithCart(product.get(), cart)));
        }
        return item;
    }

    private CartItemDto mergeProductWithCart(Product product, Cart cart) {
            var productsInCart = cart.getItems().stream()
                    .filter(item -> item.getProduct().getId().equals(product.getId()))
                    .toList();
            return  CartItemDto.builder()
                    .id(product.getId())
                    .price(product.getPrice())
                    .imgPath(product.getImgPath())
                    .title(product.getTitle())
                    .description(product.getDescription())
                    .count(productsInCart.isEmpty() ? 0 : productsInCart.getFirst().getCount())
                    .build();
    }

    private List<CartItemDto> mergeProductsWithCart(List<Product> products, Cart cart) {
        return products.stream()
                .map(product -> mergeProductWithCart(product,cart))
                .toList();
    }
}
