package ru.practicum.intershop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.intershop.entities.Order;

public interface OrderRepository  extends JpaRepository<Order, Long> {
}
