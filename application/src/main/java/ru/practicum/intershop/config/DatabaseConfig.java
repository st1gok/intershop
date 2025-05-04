package ru.practicum.intershop.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class DatabaseConfig {

    @Bean
    public Flyway flyway(
            @Value("${spring.flyway.url}") String url,
            @Value("${spring.r2dbc.username}") String username,
            @Value("${spring.r2dbc.password}") String password
    ) {

        Configuration config = new FluentConfiguration()
                .dataSource(url, username, password)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true);
        Flyway flyway = new Flyway(config);
        flyway.migrate();
        return flyway;
    }
}