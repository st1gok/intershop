package ru.practicum.intershop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.intershop.client.api.DefaultApi;

@Configuration
public class PaymentClientConfig {
    @Bean
    public DefaultApi defaultApi() {
        return new DefaultApi();
    }
}
