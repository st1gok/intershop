package ru.practicum.intershop.payment.client;

import reactor.core.publisher.Mono;

public interface PaymentClient {
    Mono<Void> pay(Double amount);

    Mono<Double> getBalance();
}
