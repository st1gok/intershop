package ru.practicum.intershop.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table(name = "carts")
@Data
public class Cart {
    @Id
    private Long id;

    @Transient
    private List<CartItem> items = new ArrayList<>();
}
