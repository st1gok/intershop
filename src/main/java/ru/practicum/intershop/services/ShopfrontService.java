package ru.practicum.intershop.services;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import ru.practicum.intershop.dto.ItemWithCartDto;
import ru.practicum.intershop.dto.ItemsWithCartDto;

public interface ShopfrontService {
    Mono<ItemsWithCartDto> getShopfrontPageWithCart(Pageable pageable, long cardId, String search);
    Mono<ItemWithCartDto> getItemWithSelectedCount(long id, long cardId);
}
