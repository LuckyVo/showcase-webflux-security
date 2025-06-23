package ru.yandex.controller;

import ru.yandex.dto.ActionDto;
import ru.yandex.dto.ItemDto;
import ru.yandex.dto.PagingDto;
import ru.yandex.dto.ViewParamDto;
import ru.yandex.mapper.ActionMapper;
import ru.yandex.mapper.ItemMapper;
import ru.yandex.service.ItemService;
import ru.yandex.service.OrderService;
import ru.yandex.utils.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static ru.yandex.utils.Utils.extractUserId;
import static ru.yandex.utils.Utils.extractUserIdToOptional;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/main/items")
@Slf4j
public class MainController {

    @Value("${app.partition-count}")
    private Integer partitionCount = 4;

    private final ItemService itemService;
    private final OrderService orderService;
    private final ItemMapper itemMapper;
    private final ActionMapper actionMapper;


    @GetMapping
    public Mono<Rendering> getItems(@AuthenticationPrincipal UserDetails user,
                                    @RequestParam(defaultValue = "") String search,
                                    @RequestParam(defaultValue = "NO") SortType sort,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "1") Integer pageNumber) {
        log.info("incoming request for getting items from user {}. search: \"{}\", sort: {}, pageNumber: {}, pageSize: {}",
                user, search, sort, pageNumber, pageSize);

        Flux<List<ItemDto>> partitionItemDto =
                itemService.findItemsWithQuantity(extractUserIdToOptional(user), search, sort, pageNumber - 1, pageSize)
                        .map(entry -> itemMapper.toDto(entry.getKey(), entry.getValue()))
                        .doOnNext(itemDto -> log.info("itemDto: {}", itemDto.getId()))
                        .buffer(partitionCount)
                        .doOnNext(it -> log.info("partitioned item dtos: {}", it));

        Mono<PagingDto> pagingDto = itemService.count(search)
                .map(count ->
                        new PagingDto(
                                pageNumber,
                                pageSize,
                                (long) pageNumber * pageSize < count,
                                pageNumber != 1
                        )
                )
                .doOnNext(it -> log.info("paging dto: {}", it));

        Rendering r = Rendering.view("main")
                .modelAttribute("items", partitionItemDto)
                .modelAttribute("paging", pagingDto)
                .modelAttribute("search", search)
                .modelAttribute("sort", sort.name())
                .modelAttribute("authenticated", user != null)
                .build();
        return Mono.just(r);
    }

    @PostMapping("/{id}")
    public Mono<String> changeItemQuantityInCart(@AuthenticationPrincipal UserDetails user,
                                                 @PathVariable long id,
                                                 @ModelAttribute ActionDto action,
                                                 @ModelAttribute ViewParamDto viewParamDto) {
        log.info("incoming request for change item quantity in cart from user {}. item id {}, action {}, viewParamDto {}",
                user, id, action, viewParamDto);

        return orderService.changeItemQuantityInCart(extractUserId(user), id, actionMapper.to(action))
                .flatMap(order ->
                        Mono.just("redirect:/main/items" +
                                "?" +
                                "search=" + viewParamDto.search + "&" +
                                "sort=" + viewParamDto.sort + "&" +
                                "pageSize=" + viewParamDto.pageSize + "&" +
                                "pageNumber=" + viewParamDto.pageNumber));

    }
}
