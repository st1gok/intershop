package ru.practicum.intershop.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.practicum.intershop.entities.UserAuthority;

@Repository
public interface UserAuthorityRepository extends R2dbcRepository<UserAuthority, Long> {
    Flux<UserAuthority> findByUserId(Long userId);
}
