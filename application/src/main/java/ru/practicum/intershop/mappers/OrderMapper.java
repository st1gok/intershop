package ru.practicum.intershop.mappers;

import org.mapstruct.Mapper;
import ru.practicum.intershop.dto.OrderDto;
import ru.practicum.intershop.entities.Order;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface OrderMapper extends EntityMapper<OrderDto, Order> {
}
