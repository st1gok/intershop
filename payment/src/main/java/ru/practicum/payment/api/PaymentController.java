package ru.practicum.payment.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import ru.practicum.payment.server.api.GetBalanceApi;
import ru.practicum.payment.server.api.PayApi;
import ru.practicum.payment.server.domain.Error;
import ru.practicum.payment.server.domain.PaymentInfo;
import ru.practicum.payment.service.NotEnoughFundsException;
import ru.practicum.payment.service.PaymentService;

import java.math.BigDecimal;

@RestController
@RequestMapping(value="/",
        produces = { "application/json" })

public class PaymentController implements PayApi, GetBalanceApi {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public Mono<PaymentInfo> getBalanceGet(ServerWebExchange exchange) {
        return paymentService.getBalance().map(amount -> new PaymentInfo().amount(new BigDecimal(amount)));
    }

    @Override
    public Mono<Void> payPost(Mono<PaymentInfo> paymentInfo, ServerWebExchange exchange) {
        return  paymentInfo.map(info -> Mono.just(info.getAmount().doubleValue()))
                .flatMap(amount -> paymentService.pay(amount));
    }

    @ExceptionHandler(NotEnoughFundsException.class)
    public Mono<ResponseEntity<Error>> handleNotEnoughFundsException(NotEnoughFundsException e) {
        return Mono.just(ResponseEntity.badRequest().body(new Error().message(e.getMessage())));
    }
}
