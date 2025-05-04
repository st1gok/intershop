package ru.practicum.payment.service;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Random;

@Component
public class BalanceRepositoryFake implements BalanceRepository {
    @Override
    public Mono<Double> getBalance() {
        return Mono.just(Double.valueOf(getRandomBalance(100, 10000)));
    }

    private Integer getRandomBalance(int min, int max) {
        return new Random().ints(min, max)
                .findFirst()
                .getAsInt();
    }

    @Override
    public Mono<Void> setBalance(Double amount) {
        return Mono.empty();
    }
}
