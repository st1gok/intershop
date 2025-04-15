package ru.practicum.intershop.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.intershop.dto.ItemsWithCartDto;
import ru.practicum.intershop.entities.Product;
import ru.practicum.intershop.repositories.ProductRepository;

import java.util.Arrays;

@SpringBootTest()
@AutoConfigureWebTestClient
class ShopfrontServiceTest {

    @Autowired
    ShopfrontService shopfrontService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll().subscribe();
    }

    @Test
    void testShopfrontPage() {
        productRepository.save(Product.builder().title("test").price(1d).build()).subscribe();
        ItemsWithCartDto shopfrontPage = shopfrontService.getShopfrontPageWithCart(PageRequest.of(0,10, Sort.unsorted()), 0, "").block();
        Assertions.assertEquals(1, shopfrontPage.getProducts().getContent().size());
        Assertions.assertEquals("test", shopfrontPage.getProducts().getContent().get(0).getTitle());
        Assertions.assertEquals(1d, shopfrontPage.getProducts().getContent().get(0).getPrice());
    }

    @Test
    void testShopfrontPageSearch() {
        productRepository.saveAll(Arrays.asList(
                Product.builder().title("Майка").price(1d).build(),
                Product.builder().title("Кепка").price(33d).build())).subscribe();
        ItemsWithCartDto shopfrontPage = shopfrontService.getShopfrontPageWithCart(PageRequest.of(0,10, Sort.unsorted()), 0, "айка").block();
        Assertions.assertEquals(1, shopfrontPage.getProducts().getContent().size());
        Assertions.assertEquals("Майка", shopfrontPage.getProducts().getContent().get(0).getTitle());
        Assertions.assertEquals(1d, shopfrontPage.getProducts().getContent().get(0).getPrice());
    }

}