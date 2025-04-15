package ru.practicum.intershop.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CartDto {
    private Long id;
    private List<CartItemDto> items = new ArrayList<>();

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Double getTotal() {
        return items.stream().map(item -> item.getPrice()*item.getCount()).reduce(0.0, Double::sum);
    }
}
