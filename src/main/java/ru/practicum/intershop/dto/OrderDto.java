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
    private List<OrderItemDto> items = new ArrayList<>();
    private Double totalSum = 0D;
}
