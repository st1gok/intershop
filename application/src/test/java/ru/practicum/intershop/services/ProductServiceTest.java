package ru.practicum.intershop.services;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import ru.practicum.intershop.AbstractTestContainerTest;
import ru.practicum.intershop.entities.Product;
import ru.practicum.intershop.repositories.ProductRepository;

import static org.mockito.Mockito.*;

public class ProductServiceTest  extends AbstractTestContainerTest {

    @MockitoSpyBean
    ProductRepository productRepository;

    @Test
    void testCacheProducts() {
        productRepository.save(Product.builder().title("test").price(1d).build()).subscribe();
        productRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("title", "title", PageRequest.of(0,10, Sort.unsorted())).collectList().block();
        productRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("title", "title", PageRequest.of(0,10, Sort.unsorted())).collectList().block();

        verify(productRepository, times(1)).findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("title", "title", PageRequest.of(0,10, Sort.unsorted()));
    }

    @Test
    void testCacheProduct() {
        productRepository.save(Product.builder().title("test").price(1d).build()).subscribe();
        productRepository.findById(1L).block();
        productRepository.findById(1L).block();

        verify(productRepository, times(1)).findById(1L);
    }
}
