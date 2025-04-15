package ru.practicum.intershop.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.CartItemDto;
import ru.practicum.intershop.dto.ItemWithCartDto;
import ru.practicum.intershop.dto.ItemsWithCartDto;
import ru.practicum.intershop.services.ShopfrontService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebFluxTest(ShopfrontController.class)
class ShopfrontControllerTest {

    @Autowired
    private WebTestClient webTestClient;

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
                Mono.just(ItemWithCartDto.builder().cartId(1l).product(Optional.of(item)).build()));
        webTestClient.get().uri("/main/items/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                });
   }

    @Test
    void testGetMainPage() throws Exception {
        Mockito.when(shopfrontService.getShopfrontPageWithCart(PageRequest.of(0,10, Sort.unsorted()), 0l ,"")).thenReturn(
                Mono.just(ItemsWithCartDto.builder().cartId(1l).products(Page.empty()).build()));
        webTestClient.get().uri("/main/items").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                });

    }
}
