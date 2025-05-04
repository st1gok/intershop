package ru.practicum.intershop.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(name = "products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String imgPath;

}
