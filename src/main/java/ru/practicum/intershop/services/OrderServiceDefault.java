package ru.practicum.intershop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.OrderDto;
import ru.practicum.intershop.dto.OrderItemDto;
import ru.practicum.intershop.entities.Order;
import ru.practicum.intershop.entities.OrderItem;
import ru.practicum.intershop.mappers.OrderMapper;
import ru.practicum.intershop.repositories.OrderItemsRepository;
import ru.practicum.intershop.repositories.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceDefault implements OrderService {

    OrderRepository repository;
    OrderMapper orderMapper;
    private final OrderItemsRepository orderItemsRepository;

    @Autowired
    public OrderServiceDefault(OrderRepository repository, OrderMapper orderMapper, OrderItemsRepository orderItemsRepository) {
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.orderItemsRepository = orderItemsRepository;
    }

    @Override
    public Mono<Page<OrderDto>> getOrders(Pageable pageable) {
        Mono<Long> countOrders = this.repository.count().cache();
        Flux<Order> orders = repository.findAllBy(pageable);
        return Mono.zip(orders.collectList(), countOrders).flatMap(tuple -> {
            var items = tuple.getT1().stream().map(orderMapper::toDto).toList();
            var pageItems = new PageImpl<>(items, pageable, tuple.getT2());
            return Mono.just(pageItems);
        });
    }

    @Override
    public Mono<OrderDto> getOrder(Long id) {
        Mono<Order> orderMono = repository.findById(id);
        Flux<OrderItem> orderItems = orderItemsRepository.findByOrderId(id);
        return Mono.zip(orderMono, orderItems.collectList()).flatMap(tuple -> {
            OrderDto orderDto = orderMapper.toDto(tuple.getT1());
            List<OrderItemDto> items = tuple.getT2().stream().map(item -> OrderItemDto.builder()
                            .orderId(item.getOrderId())
                            .productId(item.getProductId())
                            .id(item.getId())
                            .count(item.getCount())
                            .description(item.getDescription())
                            .price(item.getPrice())
                            .title(item.getTitle())
                            .imgPath(item.getImgPath())
                            .build())
                    .collect(Collectors.toList());
            orderDto.setItems(items);
            return Mono.just(orderDto);
        });
    }


}
