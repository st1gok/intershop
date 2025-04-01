package ru.practicum.intershop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.intershop.dto.CartDto;
import ru.practicum.intershop.services.CartService;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/items")
    public String getCartPage(Model model, @CookieValue(defaultValue = "0") Long cartId) {
        CartDto cartDto = cartService.getCart(cartId);
        model.addAttribute("cart", cartDto);
        return "cart";
    }

    @PostMapping("/buy")
    public String buyCart( @CookieValue(defaultValue = "0") Long cartId) {
        Long orderId = cartService.buy(cartId);
        return "redirect:/orders/" + orderId;
    }


    @PostMapping("/items/{id}")
    public String postItem(@PathVariable long id, @ModelAttribute("action") Action action, @CookieValue(defaultValue = "0") Long cartId, @RequestHeader("referer") Optional<String> referer, Model model) {
        cartService.changeCart(cartId, action, id);
        String refererUrl = referer.orElse("/main/items");
        return "redirect:" + refererUrl;
    }

    public enum Action {
        plus, minus, delete;
    }

}
