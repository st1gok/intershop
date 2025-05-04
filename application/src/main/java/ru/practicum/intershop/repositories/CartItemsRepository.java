package ru.practicum.intershop.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.practicum.intershop.entities.CartItem;

@Repository
public interface CartItemsRepository extends R2dbcRepository<CartItem, Long> {
    Flux<CartItem> findByCartId(Long cartId);
}
