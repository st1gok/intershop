package ru.practicum.intershop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.controllers.CartController;
import ru.practicum.intershop.dto.CartDto;
import ru.practicum.intershop.dto.CartItemDto;
import ru.practicum.intershop.entities.*;
import ru.practicum.intershop.mappers.CartItemMapper;
import ru.practicum.intershop.repositories.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {


    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository,
                       CartItemsRepository cartItemsRepository, OrderItemsRepository orderItemsRepository,
                       OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.orderRepository = orderRepository;
        this.cartItemsRepository = cartItemsRepository;
    }

    @Transactional
    public Mono<Void> changeCart(Long cartId, CartController.Action action, Long productId) {
        Mono<Cart> cartMono = cartRepository.findById(cartId).switchIfEmpty(cartRepository.save(new Cart()));
        var cartItems = cartItemsRepository.findByCartId(cartId).collectList();
        return Mono.zip(cartItems, cartMono).flatMap(tuple -> {
            var cart = tuple.getT1();
            CartItem cartItem = cart.stream().filter(item -> item.getProductId().equals(productId)).findFirst().orElse(new CartItem());
            cartItem.setCartId(cartId);
            if (cartItem.getProductId() == null) {
                if (CartController.Action.minus.equals(action)) {
                    return Mono.empty();
                } else {
                    cartItem.setProductId(productId);
                }
            }
            switch (action) {
                case minus:
                    if (cartItem.getCount() > 1) {
                        cartItem.setCount(cartItem.getCount() - 1);
                        return cartItemsRepository.save(cartItem);
                    } else {
                        return cartItemsRepository.deleteById(cartItem.getId());
                    }
                case plus:
                    cartItem.setCount(cartItem.getCount() + 1);
                    return cartItemsRepository.save(cartItem);
                case delete:
                    return cartItemsRepository.deleteById(cartItem.getId());
            }
            return Mono.empty();
        }).then();
    }

    public Mono<CartDto> getCart(Long cartId) {
        return cartItemsRepository.findByCartId(cartId).collectList()
                .flatMap(cartItems ->
                        Flux.fromIterable(cartItems)
                                .flatMap((cartItem) -> productRepository.findById(cartItem.getProductId()))
                                .collectList()
                                .flatMap(products -> {
                                    List<CartItemDto> cartItemsDto = products.stream().map(product -> CartItemDto.builder()
                                            .price(product.getPrice())
                                            .id(product.getId())
                                            .title(product.getTitle())
                                            .imgPath(product.getImgPath())
                                            .description(product.getDescription())
                                            .count(cartItems.stream().filter(cartItem -> cartItem.getProductId().equals(product.getId()))
                                                    .findFirst()
                                                    .orElse(new CartItem()).getCount())
                                            .build()).collect(Collectors.toList());
                                    var cartDto = new CartDto();
                                    cartDto.setId(cartId);
                                    cartDto.setItems(cartItemsDto);
                                    return Mono.just(cartDto);
                                })
                );
    }


    @Transactional
    public Mono<Long> buy(Long cartId) {
        Mono<Order> newOrder = orderRepository.save(new Order());
        Flux<CartItem> cart = cartItemsRepository.findByCartId(cartId);
        return Mono.zip(newOrder, cart.collectList()).flatMap((tuple) ->
                Flux.fromIterable(tuple.getT2())
                        .flatMap((cartItem) -> productRepository.findById(cartItem.getProductId()))
                        .collectList()
                        .flatMap(products -> {
                            var order = tuple.getT1();
                            var items = tuple.getT2();
                            order.setItems(items.stream().map(item -> {
                                var product = products.stream().filter(p -> p.getId().equals(item.getProductId())).findFirst().get();
                                return OrderItem.builder()
                                        .orderId(order.getId())
                                        .productId(item.getProductId())
                                        .price(product.getPrice())
                                        .imgPath(product.getImgPath())
                                        .description(product.getDescription())
                                        .title(product.getTitle())
                                        .count(item.getCount())
                                        .build();
                            }).toList());
                            order.setTotalSum(order.getItems().stream().mapToDouble(item -> item.getCount() * item.getPrice()).sum());
                            return orderItemsRepository.saveAll(order.getItems()).collectList()
                                    .then(cartItemsRepository.deleteAllById(items.stream().map(CartItem::getId).toList()))
                                    .then(orderRepository.save(order))
                                    .flatMap(savedOrder -> Mono.just(savedOrder.getId()));
                        })
        );
    }
}
