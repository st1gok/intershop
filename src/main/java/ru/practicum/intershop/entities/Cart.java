package ru.practicum.intershop.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(targetClass=CartItem.class,fetch= FetchType.LAZY)
    @CollectionTable(name="cart_items",joinColumns=@JoinColumn(name="cart_id"))
    private List<CartItem> items = new ArrayList<>();
}
