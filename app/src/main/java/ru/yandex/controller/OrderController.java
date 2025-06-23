package ru.yandex.controller;

import ru.yandex.dto.ItemDto;
import ru.yandex.dto.OrderDto;
import ru.yandex.entity.Item;
import ru.yandex.entity.Order;
import ru.yandex.entity.User;
import ru.yandex.mapper.ItemMapper;
import ru.yandex.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import static ru.yandex.utils.Utils.extractUserId;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final ItemMapper itemMapper;

    @GetMapping
    public Mono<Rendering> getOrders(@AuthenticationPrincipal UserDetails user) {
        log.info("incoming request for getting  from userNam {}", user);

        return orderService.findAllCompletedOrders(extractUserId(user))
                .flatMap(order -> getOrderDtoMono(extractUserId(user), order))
                .doOnNext(orderDto -> log.info("orderDto: {}", orderDto))
                .collectList()
                .map(orderDto -> Rendering.view("orders")
                        .modelAttribute("orders", orderDto)
                        .build()
                );
    }

    @GetMapping("/{id}")
    public Mono<Rendering> getOrder(@AuthenticationPrincipal UserDetails user,
                                    @PathVariable("id") long orderId,
                                    @RequestParam(defaultValue = "false") boolean newOrder,
                                    @RequestParam(defaultValue = "false") boolean rejectedOrder) {
        log.info("incoming request for getting order by orderId {} from user {}", orderId, user);

        return orderService.findById(extractUserId(user), orderId)
                .flatMap(order -> getOrderDtoMono(((User) user).getId(), order))
                .doOnNext(orderDto -> log.info("orderDto: {}", orderDto))
                .map(orderDto -> Rendering.view("order")
                        .modelAttribute("order", orderDto)
                        .modelAttribute("newOrder", newOrder)
                        .modelAttribute("rejectedOrder", rejectedOrder)
                        .build()
                );
    }

    private Mono<OrderDto> getOrderDtoMono(long userId, Order order) {
        return orderService.findItemsWithQuantityByOrderId(userId, order.getId())
                .collectMap(Entry::getKey, Entry::getValue)
                .map(map -> {
                    List<ItemDto> itemDtos = new ArrayList<>();
                    double totalSum = 0;
                    for (Entry<Item, Integer> entry : map.entrySet()) {
                        itemDtos.add(itemMapper.toDto(entry.getKey(), entry.getValue()));
                        totalSum += orderService.calcPrice(entry.getKey(), entry.getValue());
                    }
                    log.info("total sum: {} of order {}", totalSum, order);

                    return new OrderDto(order.getId(), itemDtos, totalSum);
                });
    }
}