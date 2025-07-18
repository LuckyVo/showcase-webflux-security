package ru.yandex.service;

import org.springframework.security.access.prepost.PreAuthorize;
import ru.yandex.entity.Item;
import ru.yandex.repository.ItemRepository;
import ru.yandex.repository.OrderItemRepository;
import ru.yandex.repository.OrderRepository;
import ru.yandex.utils.SortType;
import ru.yandex.utils.Status;
import ru.yandex.entity.Order;
import ru.yandex.entity.OrderItem;
import ru.yandex.exeption.ItemNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemCacheService itemCacheService;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Mono<Item> save(Item item) {
        log.info("save item: {}", item);

        return itemRepository.save(item)
                .doOnNext(savedItem -> log.info("saved item: {}", savedItem))
                .flatMap(savedItem ->
                        itemCacheService.putItem(savedItem)
                                .then(Mono.just(savedItem))
                );
    }

    @Override
    public Mono<? extends Entry<Item, Integer>> findItemWithQuantityById(Optional<Long> userId, long itemId) {
        log.info("findItemWithQuantityById. userId: {}, itemId: {}", userId, itemId);

        Mono<Item> itemFromRepoMono = Mono.defer(() ->
                itemRepository.findById(itemId)
                        .switchIfEmpty(Mono.error(() -> new ItemNotFoundException(itemId)))
                        .doOnNext(item -> log.info("found by id {} item: {}", itemId, item))
        );

        Mono<Item> itemMono = itemCacheService.getItem(itemId)
                .switchIfEmpty(itemFromRepoMono)
                .flatMap(item ->
                        itemCacheService.putItem(item)
                                .then(Mono.just(item))
                );

        Mono<Integer> quantityMono = userId.map(id ->
                        orderRepository.findByUserIdAndStatus(id, Status.CURRENT)
                                .switchIfEmpty(Mono.defer(() -> orderRepository.save(new Order(id))))
                                .next()
                                .flatMap(order -> orderItemRepository.findByItemIdAndOrderId(itemId, order.getId()))
                                .doOnNext(orderItem -> log.info("found order-item: {}", orderItem))
                                .map(OrderItem::getQuantity)
                                .defaultIfEmpty(0)
                )
                .orElseGet(() -> Mono.just(0));

        return Mono.zip(itemMono, quantityMono)
                .map(tuple -> new SimpleImmutableEntry<>(tuple.getT1(), tuple.getT2()))
                .doOnNext(entry -> log.info("item: {}, quantity: {}", entry.getKey(), entry.getValue()));
    }

    @Override
    public Flux<? extends Entry<Item, Integer>> findItemsWithQuantity(Optional<Long> userId,
                                                                      String search,
                                                                      SortType sort,
                                                                      int pageNumber,
                                                                      int pageSize) {
        log.info("findItemsWithQuantity. userId: {}, search: \"{}\", sort: {}, pageNumber: {}, pageSize: {}",
                userId, search, sort, pageNumber, pageSize);

        PageRequest pageRequest = switch (sort) {
            case NO -> PageRequest.of(pageNumber, pageSize);
            case ALPHA -> PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending());
            case PRICE -> PageRequest.of(pageNumber, pageSize, Sort.by("price").ascending());
        };
        log.info("pageRequest: {}", pageRequest);

        Mono<Map<Long, Integer>> quantityItemIdsInCurrentOrderMono = userId.map(id ->
                        orderRepository.findByUserIdAndStatus(id, Status.CURRENT)
                                .switchIfEmpty(Mono.defer(() -> orderRepository.save(new Order(id))))
                                .flatMap(order -> orderItemRepository.findByOrderId(order.getId()))
                                .collectMap(OrderItem::getItemId, OrderItem::getQuantity)
                                .doOnNext(map -> log.info("Quantity-item map in current order: {}", map))
                                .cache())
                .orElseGet(() -> Mono.just(Collections.emptyMap()));

        Flux<Item> itemFromRepoFlux = Flux.defer(() -> {
            if (search.isEmpty()) {
                return itemRepository.findAllBy(pageRequest);
            } else {
                return itemRepository.findByTitleOrDescriptionContains(search, pageRequest);
            }
        });

        String fullKey = generateCacheKey(search, sort, pageNumber, pageSize);

        Flux<Item> itemFlux = itemCacheService.getItemList(fullKey)
                .switchIfEmpty(itemFromRepoFlux)
                .collectList()
                .flatMap(items ->
                        itemCacheService.putItemList(fullKey, items)
                                .then(Mono.just(items))
                )
                .flatMapMany(Flux::fromIterable);

        return itemFlux
                .doOnNext(item -> log.info("found item: {}", item))
                .concatMap(item ->
                        quantityItemIdsInCurrentOrderMono.map(quantityItemsMap ->
                                new SimpleImmutableEntry<>(item, quantityItemsMap.getOrDefault(item.getId(), 0))
                        ))
                .doOnNext(entry -> log.info("item {} quantity: {}", entry.getKey(), entry.getValue()));
    }

    static String generateCacheKey(String search,
                                   SortType sort,
                                   int pageNumber,
                                   int pageSize) {
        return new StringJoiner("-")
                .add(search.isEmpty() ? "EMPTY" : search)
                .add(sort.name())
                .add(Integer.toString(pageNumber))
                .add(Integer.toString(pageSize))
                .toString();

    }

    @Override
    public Mono<Long> count(String search) {
        log.info("count of items by search string \"{}\"", search);

        Mono<Long> countFromRepoMono = Mono.defer(() -> {
            if (search.isEmpty()) {
                return itemRepository.count()
                        .doOnNext(count -> log.info("item count: {}", count));
            } else {
                return itemRepository.countByTitleOrDescriptionContains(search)
                        .doOnNext(count -> log.info("item count: {}, search string: {}", count, search));
            }
        });

        String fullKey = generateCacheKey(search);

        return itemCacheService.getItemCount(fullKey)
                .switchIfEmpty(countFromRepoMono)
                .flatMap(count ->
                        itemCacheService.putItemCount(fullKey, count)
                                .then(Mono.just(count))
                );
    }

    static String generateCacheKey(String str) {
        return str.isEmpty() ? "EMPTY" : str;
    }
}
