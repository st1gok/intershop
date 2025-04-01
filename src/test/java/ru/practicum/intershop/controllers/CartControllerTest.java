package ru.practicum.intershop.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.intershop.dto.CartDto;
import ru.practicum.intershop.services.CartService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    private CartService cartService;

    @Test
    public void testCartController() throws Exception {
        CartDto cartDto = new CartDto();
        cartDto.setId(1);
        Mockito.when(cartService.getCart(0l)).thenReturn(
                cartDto);
        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cart"));
    }
}
