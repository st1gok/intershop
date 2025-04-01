package ru.practicum.intershop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.intershop.dto.ProductDto;
import ru.practicum.intershop.mappers.ProductMapper;
import ru.practicum.intershop.repositories.ProductRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductsService {

    ProductRepository productRepository;
    ProductMapper productMapper;

    @Autowired
    ProductsService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public void importProducts(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<ProductDto> records = reader.lines()
                    .map(line -> {
                        var productLine = Arrays.asList(line.split(";"));
                        return ProductDto.builder()
                                .title(productLine.get(0))
                                .description(productLine.get(1))
                                .price(Double.valueOf(productLine.get(2)))
                                .imgPath(productLine.size()>3 ? "/img/" + productLine.get(3) : null)
                                .build();
                    }).collect(Collectors.toList());
            productRepository.saveAll(records.stream().map(productMapper::toEntity).toList());
        }
    }
}
