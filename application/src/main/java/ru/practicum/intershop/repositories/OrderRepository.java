package ru.practicum.intershop.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.entities.Order;


public interface OrderRepository  extends R2dbcRepository<Order, Long> {
    Flux<Order> findAllByUserId(Long userId, Pageable pageable);

    Mono<Order> findByIdAndUserId(Long id, Long userId);
}
