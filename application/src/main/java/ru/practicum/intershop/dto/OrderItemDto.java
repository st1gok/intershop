package ru.practicum.intershop.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class OrderItemDto extends CartItemDto {
    private Long orderId;
    private Long productId;
}
