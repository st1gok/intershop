package ru.practicum.intershop.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.intershop.dto.ItemWithCartDto;
import ru.practicum.intershop.dto.ItemsWithCartDto;

public interface ShopfrontService {
    ItemsWithCartDto getShopfrontPageWithCart(Pageable pageable, long cardId, String search);
    ItemWithCartDto getItemWithSelectedCount(long id, long cardId);

}
