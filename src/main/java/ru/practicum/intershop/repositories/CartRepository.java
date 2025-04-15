package ru.practicum.intershop.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.practicum.intershop.entities.Cart;

public interface CartRepository  extends R2dbcRepository<Cart, Long> {
}
