package ru.practicum.intershop.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class ProductDto {
    Long id;
    String title;
    String description;
    Double price;
    String imgPath;
}
