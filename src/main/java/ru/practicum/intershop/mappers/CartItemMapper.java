package ru.practicum.intershop.mappers;

import org.mapstruct.Mapper;
import ru.practicum.intershop.dto.CartItemDto;
import ru.practicum.intershop.entities.CartItem;
import ru.practicum.intershop.entities.Product;

@Mapper(componentModel = "spring", uses = {})
public interface CartItemMapper extends EntityMapper<CartItemDto, CartItem> {
    @Override
    default CartItemDto toDto(CartItem entity) {
        return CartItemDto.builder()
                .id(entity.getProduct().getId())
                .price(entity.getProduct().getPrice())
                .imgPath(entity.getProduct().getImgPath())
                .title(entity.getProduct().getTitle())
                .count(entity.getCount())
                .description(entity.getProduct().getDescription())
                .build();
    }

    default CartItem toEntity(CartItemDto dto) {
        CartItem item = new CartItem();
        item.setCount(dto.getCount());
        Product product = new Product();
        product.setId(dto.getId());
        product.setPrice(dto.getPrice());
        product.setImgPath(dto.getImgPath());
        product.setTitle(dto.getTitle());
        product.setDescription(dto.getDescription());
        item.setProduct(product);
        return item;
    }
}
