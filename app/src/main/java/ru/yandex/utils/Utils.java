package ru.yandex.utils;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.entity.User;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static Optional<Long> extractUserId(User user) {
        return Optional.ofNullable(user == null ? null : user.getId());
    }
}
