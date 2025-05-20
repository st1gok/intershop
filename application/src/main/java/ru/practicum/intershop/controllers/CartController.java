package ru.practicum.intershop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.services.CartService;
import ru.practicum.intershop.services.UserService;

import java.net.URI;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/items")
    public Mono<String> getCartPage(Model model, @CookieValue(defaultValue = "0") Long cartId) {
        return userService.getUserWithAuthorities().flatMap(user-> cartService.getUserCart(user.getId())).zipWith(cartService.getBalance())
                .doOnNext(tuple -> model.addAttribute("cart", tuple.getT1())
                        .addAttribute("balance", tuple.getT2()))
                .map(cart -> "cart");
    }

    @PostMapping("/buy")
    public Mono<ResponseEntity<Void>> buyCart( @CookieValue(defaultValue = "0") Long cartId) {
       return userService.getUserWithAuthorities().flatMap(user-> cartService.buyItemsFromUserCart(user.getId())).flatMap(orderId -> Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/orders/" + orderId))
                .build()));
    }


    @PostMapping("/items/{id}")
    public Mono<ResponseEntity<Void>> postItem(@PathVariable long id, ServerWebExchange exchange, @CookieValue(defaultValue = "0") Long cartId, @RequestHeader("referer") Optional<String> referer, Model model) {
        return Mono.zip(exchange.getFormData(), userService.getUserWithAuthorities()).flatMap((form) -> cartService.changeCart(form.getT2().getId(), Action.valueOf(form.getT1().getFirst("action")), id)).then(
                Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(referer.orElse("/main/items")))
                    .build()));
    }

    public enum Action {
        plus, minus, delete;
    }

}
