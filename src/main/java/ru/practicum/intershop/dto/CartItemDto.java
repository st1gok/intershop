package ru.practicum.intershop.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class CartItemDto extends ProductDto {
    Integer count;
}
