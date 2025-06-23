package ru.yandex.service;

import ru.yandex.entity.User;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> save(User user);

    Mono<User> lock(long id);

    Mono<User> addRole(long id, String role);

    Mono<User> removeRole(long id, String role);
}
