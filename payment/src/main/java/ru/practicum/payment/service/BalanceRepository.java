package ru.practicum.payment.service;

import reactor.core.publisher.Mono;

public interface BalanceRepository {

    Mono<Double> getBalance();

    Mono<Void> setBalance(Double amount);
}
