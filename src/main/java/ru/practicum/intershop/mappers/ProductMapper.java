package ru.practicum.intershop.mappers;

import org.mapstruct.Mapper;
import ru.practicum.intershop.dto.ProductDto;
import ru.practicum.intershop.entities.Product;

@Mapper(componentModel = "spring", uses = {})
public interface ProductMapper  extends EntityMapper<ProductDto, Product> {

}
