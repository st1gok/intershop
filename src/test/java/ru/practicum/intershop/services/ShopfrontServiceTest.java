package ru.practicum.intershop.services;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.intershop.dto.ItemsWithCartDto;

@SpringBootTest
@Transactional
class ShopfrontServiceTest {

    @Autowired
    ShopfrontService shopfrontService;

    @Test
    @Sql(statements = "insert into products(title, price) values('test', 1)")
    void testShopfrontPage() {
        ItemsWithCartDto shopfrontPage = shopfrontService.getShopfrontPageWithCart(PageRequest.of(0,10, Sort.unsorted()), 0, "");
        Assertions.assertEquals(1, shopfrontPage.getProducts().getContent().size());
        Assertions.assertEquals("test", shopfrontPage.getProducts().getContent().get(0).getTitle());
        Assertions.assertEquals(1d, shopfrontPage.getProducts().getContent().get(0).getPrice());
    }

    @Test
    @Sql(statements = "insert into products(title, price) values('Майка', 1),('Кепка', 33)")
    void testShopfrontPageSearch() {
        ItemsWithCartDto shopfrontPage = shopfrontService.getShopfrontPageWithCart(PageRequest.of(0,10, Sort.unsorted()), 0, "айка");
        Assertions.assertEquals(1, shopfrontPage.getProducts().getContent().size());
        Assertions.assertEquals("Майка", shopfrontPage.getProducts().getContent().get(0).getTitle());
        Assertions.assertEquals(1d, shopfrontPage.getProducts().getContent().get(0).getPrice());
    }

}