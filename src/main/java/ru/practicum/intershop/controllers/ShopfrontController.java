package ru.practicum.intershop.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.practicum.intershop.dto.ItemWithCartDto;
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
    public String getMainPage(Pageable pageable, @RequestParam(required = false, defaultValue = "") String search,  @CookieValue(defaultValue = "0") Long cartId, HttpServletResponse response, Model model) {
        var content = shopfrontService.getShopfrontPageWithCart(pageable, cartId, search.trim());
        Cookie browserSessionCookie = new Cookie("cartId",String.valueOf(content.getCartId()));
        browserSessionCookie.setPath("/");
        response.addCookie(browserSessionCookie);
        model.addAttribute("items", content.getProducts());
        model.addAttribute("paging", content.getProducts().getPageable());
        model.addAttribute("search", search);
        if (content.getProducts().getSort().isSorted()) {
            model.addAttribute("sort", content.getProducts().getSort().get().findFirst().get().getProperty());
        }
        return "main";
}

    @GetMapping("/{id}")
    public String getItemPage(@PathVariable long id, @CookieValue(defaultValue = "0") Long cartId, Model model, HttpServletResponse response) {
        ItemWithCartDto item = shopfrontService.getItemWithSelectedCount(id, cartId);
        if (item.getProduct().isPresent()) {
            Cookie browserSessionCookie = new Cookie("cartId",String.valueOf(item.getCartId()));
            browserSessionCookie.setPath("/");
            response.addCookie(browserSessionCookie);
            model.addAttribute("item", item.getProduct().get());
            return "item";
        } else {
            return "redirect:/main/items/";
        }
    }


}
