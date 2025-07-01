package ru.yandex.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.yandex.dto.ActionDto;
import ru.yandex.entity.User;
import ru.yandex.mapper.ActionMapper;
import ru.yandex.mapper.ItemMapper;
import ru.yandex.service.ItemService;
import ru.yandex.service.OrderService;

import static ru.yandex.utils.Utils.extractUserId;

@Controller
@RequestMapping(value = "/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final OrderService orderService;

    private final ItemMapper itemMapper;
    private final ActionMapper actionMapper;

    @GetMapping("/{id}")
    public Mono<Rendering> getItem(@AuthenticationPrincipal User user, @PathVariable("id") long itemId) {
        log.info("incoming request for getting item by itemId {} from user {}", itemId, user);

        return itemService.findItemWithQuantityById(extractUserId(user), itemId)
                .map(item -> itemMapper.toDto(item.getKey(), item.getValue()))
                .doOnNext(itemDto -> log.info("item dto: {}", itemDto))
                .map(dto -> Rendering.view("item")
                        .modelAttribute("item", dto)
                        .modelAttribute("authenticated", user != null)
                        .build()
                );
    }

    @PostMapping("/{id}")
    public Mono<String> changeItemQuantityInCart(@AuthenticationPrincipal User user,
                                                 @PathVariable int id,
                                                 @ModelAttribute ActionDto action) {
        log.info("incoming request for change item quantity in cart from user {}. item id {}, action {} ",
                user, id, action);

        return orderService.changeItemQuantityInCart(user.getId(), id, actionMapper.to(action))
                .flatMap(order -> Mono.just("redirect:/items/" + id));
    }
}
