package ru.yandex.service;

import ru.yandex.entity.Item;
import ru.yandex.utils.SortType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map.Entry;
import java.util.Optional;

public interface ItemService {

    Mono<Item> save(Item item);

    Mono<? extends Entry<Item, Integer>> findItemWithQuantityById(Optional<Long> userId, long itemId);

    Flux<? extends Entry<Item, Integer>> findItemsWithQuantity(Optional<Long> userId,
                                                               String search,
                                                               SortType sort,
                                                               int pageNumber,
                                                               int pageSize);

    Mono<Long> count(String search);
}
