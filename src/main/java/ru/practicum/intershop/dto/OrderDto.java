package ru.practicum.intershop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDto {
    private Long id;
    private List<CartItemDto> items = new ArrayList<>();

    public Double getTotalSum() {
        return items.stream().map(CartItemDto::getPrice).reduce(0.0, Double::sum);
    }
}
