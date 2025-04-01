package ru.practicum.intershop.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import ru.practicum.intershop.dto.OrderDto;
import ru.practicum.intershop.dto.CartItemDto;
import ru.practicum.intershop.services.OrderService;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    OrderService orderService;

    @Test
    void testGetOrders() throws Exception {
        Mockito.when(orderService.getOrders(PageRequest.of(0,10, Sort.unsorted()))).thenReturn(
                Page.empty());
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void testGetOrder() throws Exception {
        CartItemDto cartItemDto = CartItemDto.builder()
                .price(5d)
                .count(1)
                .id(1l)
                .build();
        Mockito.when(orderService.getOrder(1l)).thenReturn(
                Optional.ofNullable(OrderDto.builder().id(1l).items(Arrays.asList(cartItemDto)).build()));
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"));
    }
}
