package ru.practicum.intershop.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.entities.Product;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {
    @Cacheable(
            value = "products"
    )
    Flux<Product> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);

    @Cacheable(
            value = "product"
    )
    Mono<Product> findById(Long id);

    <S extends Product> Flux<S> saveAll(Iterable<S> entities);
}
