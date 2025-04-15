package ru.practicum.intershop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Mono<String> getOrdersPage(Pageable pageable, Model model) {
        return orderService.getOrders(pageable)
                .doOnNext(orders -> model.addAttribute("orders", orders))
                .map(orders -> "orders");
    }

    @GetMapping("/{id}")
    public Mono<String> getOrderPage(@PathVariable long id, Model model) {
        return orderService.getOrder(id)
                .doOnNext(order -> model.addAttribute("order", order))
                .map(order -> "order");
    }

}
