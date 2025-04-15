package ru.practicum.intershop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.services.ShopfrontService;

@Controller
@RequestMapping("/main/items")
public class ShopfrontController {

    private final ShopfrontService shopfrontService;

    @Autowired
    public ShopfrontController(ShopfrontService shopfrontService) {
        this.shopfrontService = shopfrontService;
    }

    @GetMapping
    public Mono<String> getMainPage(Pageable pageable, @RequestParam(required = false, defaultValue = "") String search, @CookieValue(defaultValue = "0") Long cartId, ServerHttpResponse response, Model model) {
        return shopfrontService.getShopfrontPageWithCart(pageable, cartId, search.trim())
                .doOnNext(content -> {
                            if ((cartId) == 0) {
                                response.addCookie(ResponseCookie.from("cartId", String.valueOf(content.getCartId())).path("/").build());
                            }
                            model.addAttribute("items", content.getProducts());
                            model.addAttribute("paging", content.getProducts().getPageable());
                            model.addAttribute("search", search);
                            if (content.getProducts().getSort().isSorted()) {
                                model.addAttribute("sort", content.getProducts().getSort().get().findFirst().get().getProperty());
                            }
                        }
                )
                .map(main -> "main");
    }

    @GetMapping("/{id}")
    public Mono<String> getItemPage(@PathVariable long id, @CookieValue(defaultValue = "0") Long cartId, ServerHttpResponse response, Model model) {
        return shopfrontService.getItemWithSelectedCount(id, cartId)
                .doOnNext(item -> {
                    if ((cartId) == 0) {
                        response.addCookie(ResponseCookie.from("cartId", String.valueOf(item.getCartId())).path("/").build());
                    }
                    model.addAttribute("item", item.getProduct().get());
                })
                .map(item -> "item").switchIfEmpty(Mono.just("redirect:/main/items"));
    }


}
