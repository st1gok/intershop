package ru.practicum.intershop.payment.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.client.api.DefaultApi;
import ru.practicum.intershop.client.domain.PaymentInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PaymentClientAdapter implements PaymentClient {

    private final DefaultApi api;
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;


    @Autowired
    public PaymentClientAdapter(DefaultApi api, ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        this.api = api;
        this.authorizedClientManager = authorizedClientManager;
    }

    private Mono<String> getToken() {
        return authorizedClientManager.authorize(OAuth2AuthorizeRequest
                        .withClientRegistrationId("keycloak")
                        .principal("system")
                        .build()) // Mono<OAuth2AuthorizedClient>
                .map(OAuth2AuthorizedClient::getAccessToken)
                .map(OAuth2AccessToken::getTokenValue);
    }

    @Override
    public Mono<Void> pay(Double amount) {
        return getToken().flatMap(accessToken -> {
                    System.out.println("accessToken: " + accessToken);
                    api.setApiClient(api.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
                    return api.payPost(new PaymentInfo().amount(new BigDecimal(amount).setScale(2, RoundingMode.CEILING)));
                });
    }

    @Override
    public Mono<Double> getBalance() {
        return getToken().flatMap(accessToken -> {
                            api.setApiClient(api.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken));
                            return api.balanceGet().
                                    map(paymentInfo -> paymentInfo.getAmount().setScale(2, RoundingMode.CEILING).doubleValue());
                        }
                );
    }
}
