package ru.practicum.intershop.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class ItemsWithCartDto {
    Page<CartItemDto> products;
    Long cartId;
}
