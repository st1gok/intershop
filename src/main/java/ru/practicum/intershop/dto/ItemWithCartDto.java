package ru.practicum.intershop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class ItemWithCartDto {
    Optional<CartItemDto> product;
    Long cartId;
}
