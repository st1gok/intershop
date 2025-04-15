package ru.practicum.intershop.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "products")
@Builder
public class Product {
    @Id
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String imgPath;

}
