package ru.practicum.intershop.payment.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.api.DefaultApi;
import ru.practicum.intershop.client.domain.PaymentInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PaymentClientAdapter implements PaymentClient {

    private final DefaultApi api;

    @Autowired
    public PaymentClientAdapter(DefaultApi api) {
        this.api = api;
    }

    @Override
    public Mono<Void> pay(Double amount) {
        return api.payPost(new PaymentInfo().amount(new BigDecimal(amount).setScale(2, RoundingMode.CEILING)));
    }

    @Override
    public Mono<Double> getBalance() {
        return api.getBalanceGet().
                map(paymentInfo -> paymentInfo.getAmount().setScale(2, RoundingMode.CEILING).doubleValue());
    }
}
