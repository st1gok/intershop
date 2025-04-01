package ru.practicum.intershop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.intershop.dto.OrderDto;
import ru.practicum.intershop.mappers.OrderMapper;
import ru.practicum.intershop.repositories.OrderRepository;

import java.util.Optional;

@Service
public class OrderServiceDefault implements OrderService {

    OrderRepository repository;
    OrderMapper orderMapper;

    @Autowired
    public OrderServiceDefault(OrderRepository repository, OrderMapper orderMapper) {
        this.repository = repository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Page<OrderDto> getOrders(Pageable pageable) {
        return repository.findAll(pageable).map(orderMapper::toDto);
    }

    @Override
    public Optional<OrderDto> getOrder(Long id) {
        return repository.findById(id).map(orderMapper::toDto);
    }


}
