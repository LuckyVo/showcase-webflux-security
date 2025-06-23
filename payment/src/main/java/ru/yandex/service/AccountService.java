package ru.yandex.service;

import ru.yandex.entity.Account;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<Account> save(Account account);

    Mono<Double> getBalanceByUserId(long userId);

    Mono<Account> payment(long userId, double amount);
}
