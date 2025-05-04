package ru.practicum.payment.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentServiceDefault implements PaymentService {

    private final BalanceRepository balanceStorage;

    public PaymentServiceDefault(BalanceRepository balanceStorage) {
        this.balanceStorage = balanceStorage;
    }

    @Override
    public Mono<Double> getBalance() {
        return balanceStorage.getBalance();
    }

    @Override
    public Mono<Void> pay(Mono<Double> amount) {
        return Mono.zip(getBalance(), amount)
                .flatMap(tuple -> {
                    Double newBalance = tuple.getT1() - tuple.getT2();
                    return newBalance < 0 ? Mono.error(new NotEnoughFundsException()) : balanceStorage.setBalance(newBalance);
                });
    }

}
