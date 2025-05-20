package ru.practicum.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain filterChain(ServerHttpSecurity http, ReactiveJwtAuthenticationConverter authenticationConverter) throws Exception {
        http.authorizeExchange(requests -> requests
                        .anyExchange().authenticated()
                );
    http.oauth2ResourceServer(resourceServer -> resourceServer.jwt(jwtResourceServer ->
        jwtResourceServer.jwtAuthenticationConverter(authenticationConverter)
    ));
        http.csrf().disable();
        return http.build();
    }


    @Bean
    ReactiveJwtAuthenticationConverter authenticationConverter() {
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
                            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {

                                Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
                                Map<String, Object> account = (Map<String, Object>) resourceAccess.get("account");
                                List<String> roles = (List<String>) account.get("roles");

                                return Flux.fromIterable(roles)
                                        .map(SimpleGrantedAuthority::new);
                            });
                            return jwtAuthenticationConverter;
    }
}
