package ru.practicum.intershop.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.OrderDto;

public interface OrderService {
    Mono<Page<OrderDto>> getOrders(Pageable pageable);

    Mono<OrderDto> getOrder(Long id);
}
