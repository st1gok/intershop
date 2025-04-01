package ru.practicum.intershop.mappers;

import org.mapstruct.Mapper;
import ru.practicum.intershop.dto.CartDto;
import ru.practicum.intershop.entities.Cart;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper extends EntityMapper<CartDto, Cart> {
}
