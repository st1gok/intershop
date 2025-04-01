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
import ru.practicum.intershop.dto.CartItemDto;
import ru.practicum.intershop.dto.ItemWithCartDto;
import ru.practicum.intershop.dto.ItemsWithCartDto;
import ru.practicum.intershop.services.ShopfrontService;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(ShopfrontController.class)
class ShopfrontControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ShopfrontService shopfrontService;

    @Test
    void testGetItemPage() throws Exception {
        CartItemDto item = CartItemDto.builder()
                .id(1l)
                .title("title")
                .description("description")
                .count(0)
                .price(20d)
                .build();
        Mockito.when(shopfrontService.getItemWithSelectedCount(1l, 0l )).thenReturn(
                ItemWithCartDto.builder().cartId(1l).product(Optional.of(item)).build());
        mockMvc.perform(get("/main/items/1"))
               .andExpect(status().isOk())
               .andExpect(content().contentType("text/html;charset=UTF-8"))
               .andExpect(view().name("item"))
               .andExpect(model().attributeExists("item"));
   }

    @Test
    void testGetMainPage() throws Exception {
        Mockito.when(shopfrontService.getShopfrontPageWithCart(PageRequest.of(0,10, Sort.unsorted()), 0l ,"")).thenReturn(
                ItemsWithCartDto.builder().cartId(1l).products(Page.empty()).build());
        mockMvc.perform(get("/main/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main"))
                .andExpect(model().attributeExists("items", "paging", "search"));

    }
}
