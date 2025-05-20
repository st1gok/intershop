package ru.practicum.intershop.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Data
public class Order {
    @Id
    private Long id;
    private Long userId;
    private Double totalSum = 0d;

    @Transient
    private List<OrderItem> items = new ArrayList<>();
}
