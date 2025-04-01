package ru.practicum.intershop.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.intershop.controllers.CartController;
import ru.practicum.intershop.dto.CartDto;
import ru.practicum.intershop.entities.Cart;
import ru.practicum.intershop.entities.CartItem;
import ru.practicum.intershop.entities.Order;
import ru.practicum.intershop.entities.Product;
import ru.practicum.intershop.mappers.CartMapper;
import ru.practicum.intershop.repositories.CartRepository;
import ru.practicum.intershop.repositories.OrderRepository;
import ru.practicum.intershop.repositories.ProductRepository;

@Service
public class CartService {


    CartRepository cartRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    CartMapper cartMapper;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository,
                       CartMapper cartMapper, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void changeCart(Long cartId, CartController.Action action, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElse(cartRepository.save(new Cart()));
        CartItem cartItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().orElse(new CartItem());
        if (cartItem.getProduct() == null) {
            if (CartController.Action.minus.equals(action)) {
                return;
            } else {
                Product product = productRepository.findById(productId).get();
                cartItem.setProduct(product);
                cart.getItems().add(cartItem);
            }
        }
        switch (action) {
            case minus:
                if (cartItem.getCount() > 1) {
                    cartItem.setCount(cartItem.getCount() - 1);
                } else {
                    cart.getItems().remove(cartItem);
                }
                break;
            case plus:
                cartItem.setCount(cartItem.getCount() + 1);
                break;
            case delete:
                cart.getItems().remove(cartItem);
                break;
        }
        cartRepository.save(cart);
    }

    public CartDto getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(cartRepository.save(new Cart()));
        return cartMapper.toDto(cart);
    }


    @Transactional
    public Long buy(Long cartId) {
        Cart cart = cartRepository.findById(cartId).get();
        Order order = new Order();
        order.setItems(cart.getItems());
        cartRepository.delete(cart);
        return orderRepository.save(order).getId();
    }
}
