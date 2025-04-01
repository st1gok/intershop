package ru.practicum.intershop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.intershop.entities.Cart;

public interface CartRepository  extends JpaRepository<Cart, Long> {
}
