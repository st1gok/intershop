package ru.practicum.intershop.mappers;

import org.mapstruct.Mapper;
import ru.practicum.intershop.dto.CartItemDto;
import ru.practicum.intershop.entities.CartItem;

@Mapper(componentModel = "spring", uses = {})
public interface CartItemMapper extends EntityMapper<CartItemDto, CartItem> {
    @Override
    default CartItemDto toDto(CartItem entity) {
        return CartItemDto.builder()
                .id(entity.getProductId())
                .count(entity.getCount())
                .build();
    }

    default CartItem toEntity(CartItemDto dto) {
        CartItem item = new CartItem();
        item.setCount(dto.getCount());
        item.setProductId(dto.getId());
        return item;
    }
}
