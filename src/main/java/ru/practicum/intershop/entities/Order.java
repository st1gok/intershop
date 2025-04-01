package ru.practicum.intershop.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(targetClass=CartItem.class,fetch= FetchType.LAZY)
    @CollectionTable(name="order_items",joinColumns=@JoinColumn(name="order_id"))
    private List<CartItem> items = new ArrayList<>();
}
