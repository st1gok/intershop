package ru.practicum.intershop.controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.CartDto;
import ru.practicum.intershop.entities.User;
import ru.practicum.intershop.services.CartService;
import ru.practicum.intershop.services.UserService;

@WebFluxTest( controllers = CartController.class,  excludeAutoConfiguration = {
        ReactiveOAuth2ClientAutoConfiguration.class,
        ReactiveOAuth2ResourceServerAutoConfiguration.class,
        })
@WithMockUser(username = "user")
public class CartControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private UserService userService;

    @Test
    public void testCartController() throws Exception {
        CartDto cartDto = new CartDto();
        cartDto.setId(1l);
        Mockito.when(cartService.getBalance()).thenReturn(Mono.just(
                1000D));
        Mockito.when(userService.getUserWithAuthorities()).thenReturn(Mono.just(
                User.builder().id(1l).login("user").build()));
        Mockito.when(cartService.getUserCart(1l)).thenReturn(Mono.just(
                cartDto));

        webTestClient.get().uri("/cart/items").exchange()
                .expectStatus().isOk()
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                });
    }
}
