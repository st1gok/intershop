package ru.practicum.intershop.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.entities.User;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {
    Mono<User> findOneByLogin(String login);
}
