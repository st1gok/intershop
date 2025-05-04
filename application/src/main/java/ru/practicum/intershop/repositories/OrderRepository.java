package ru.practicum.intershop.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import ru.practicum.intershop.entities.Order;


public interface OrderRepository  extends R2dbcRepository<Order, Long> {
    Flux<Order> findAllBy(Pageable pageable);
}
