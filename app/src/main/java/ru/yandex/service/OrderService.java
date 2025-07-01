package ru.yandex.service;


import ru.yandex.entity.Item;
import ru.yandex.utils.Action;
import ru.yandex.entity.Order;
import ru.yandex.entity.OrderItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map.Entry;

public interface OrderService {

    Mono<Order> save(Order order);

    Flux<Order> findAllCompletedOrders(long userId);

    Mono<Order> findById(long orderId);

    Mono<Order> findCurrentOrder(long userId);

    Flux<? extends Entry<Item, Integer>> findItemsWithQuantityByOrderId(long userId, long orderId);

    Mono<Order> completeCurrentOrder(long userId);

    Mono<OrderItem> changeItemQuantityInCart(long userId, long itemId, Action action);

    double calcPrice(Item item, int quantity);
}
