package ru.practicum.payment.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.practicum.payment.server.domain.PaymentInfo;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@AutoConfigureWebTestClient
class PaymentControllerTest {
    @Autowired
    private WebTestClient webTestClient;



    @Test
    public void getBalance() {
        webTestClient.get().uri("/getBalance").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PaymentInfo.class).consumeWith(response -> {
                    PaymentInfo body = response.getResponseBody();
                    assertTrue(body.getAmount().doubleValue() > 0);
                });
    }

    @Test
    public void pay() {
        webTestClient.post().uri("/pay").body(Mono.just(new PaymentInfo().amount(BigDecimal.ONE)), PaymentInfo.class).exchange()
                .expectStatus().isOk();
    }

    @Test
    public void notEnoughBalance() {
        webTestClient.post().uri("/pay").body(Mono.just(new PaymentInfo().amount(BigDecimal.valueOf(100000))), PaymentInfo.class).exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }
}