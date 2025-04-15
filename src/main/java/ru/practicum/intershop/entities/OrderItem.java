package ru.practicum.intershop.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "order_items")
@Builder
public class OrderItem {

    @Id
    private Long id;
    private Long orderId;
    private Long productId;
    private String title;
    private String description;
    private Double price;
    private String imgPath;
    private Integer count = 0;
}
