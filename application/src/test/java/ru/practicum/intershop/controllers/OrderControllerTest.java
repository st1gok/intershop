package ru.practicum.intershop.controllers;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockAuthentication;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.OrderDto;
import ru.practicum.intershop.dto.OrderItemDto;
import ru.practicum.intershop.services.OrderService;

import java.util.Arrays;

@WebFluxTest(controllers = OrderController.class,  excludeAutoConfiguration = {
        ReactiveOAuth2ClientAutoConfiguration.class,
        ReactiveOAuth2ResourceServerAutoConfiguration.class,
})
@WithMockAuthentication
class OrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    OrderService orderService;

    @Test
    void testGetOrders() throws Exception {
        Mockito.when(orderService.getOrders(PageRequest.of(0,10, Sort.unsorted()))).thenReturn(Mono.just(
                Page.empty()));
        webTestClient.get().uri("/orders").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                });
    }

    @Test
    void testGetOrder() throws Exception {
        OrderItemDto orderItemDto = OrderItemDto.builder()
                .price(5d)
                .count(1)
                .id(1l)
                .build();
        Mockito.when(orderService.getOrder(1l)).thenReturn(
               Mono.just(OrderDto.builder().id(1l).items(Arrays.asList(orderItemDto)).build()));
        webTestClient.get().uri("/orders/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.TEXT_HTML)
                .expectBody(String.class).consumeWith(response -> {
                    String body = response.getResponseBody();
                    assertNotNull(body);
                });
    }
}
