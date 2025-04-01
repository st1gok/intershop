package ru.practicum.intershop.entities;

import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class CartItem {

    @OneToOne
    private Product product;
    private Integer count = 0;
}
