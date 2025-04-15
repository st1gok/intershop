package ru.practicum.intershop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.services.CartService;

import java.net.URI;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/items")
    public Mono<String> getCartPage(Model model, @CookieValue(defaultValue = "0") Long cartId) {
        return cartService.getCart(cartId)
                .doOnNext(cart -> model.addAttribute("cart", cart))
                .map(cart -> "cart");
    }

    @PostMapping("/buy")
    public Mono<ResponseEntity<Void>> buyCart( @CookieValue(defaultValue = "0") Long cartId) {
       return cartService.buy(cartId).flatMap(orderId -> Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/orders/" + orderId))
                .build()));
    }


    @PostMapping("/items/{id}")
    public Mono<ResponseEntity<Void>> postItem(@PathVariable long id, ServerWebExchange exchange, @CookieValue(defaultValue = "0") Long cartId, @RequestHeader("referer") Optional<String> referer, Model model) {
        return exchange.getFormData().flatMap((form) -> cartService.changeCart(cartId, Action.valueOf(form.getFirst("action")), id)).then(
                Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(referer.orElse("/main/items")))
                    .build()));
    }

    public enum Action {
        plus, minus, delete;
    }

}
