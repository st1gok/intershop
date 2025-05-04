package ru.practicum.intershop.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table(name = "cart_items")
public class CartItem {

    @Id
    private Long id;
    private Long cartId;
    private Long productId;
    private Integer count = 0;
}
