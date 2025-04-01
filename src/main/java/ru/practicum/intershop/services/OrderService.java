package ru.practicum.intershop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.intershop.dto.OrderDto;

import java.util.Optional;

public interface OrderService {
    Page<OrderDto> getOrders(Pageable pageable);
    Optional<OrderDto> getOrder(Long id);
}
