package ru.practicum.intershop.controllers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.CartDto;
import ru.practicum.intershop.services.CartService;

@WebFluxTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private CartService cartService;

    @Test
    public void testCartController() throws Exception {
        CartDto cartDto = new CartDto();
        cartDto.setId(1l);
        Mockito.when(cartService.getBalance()).thenReturn(Mono.just(
                1000D));
        Mockito.when(cartService.getCart(0l)).thenReturn(Mono.just(
                cartDto));

        webTestClient.get().uri("/cart/items").exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                });
    }
}
