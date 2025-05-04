package ru.practicum.payment.service;

import reactor.core.publisher.Mono;

public interface PaymentService {

    Mono<Double> getBalance();
    Mono<Void> pay(Mono<Double> amount);
}
