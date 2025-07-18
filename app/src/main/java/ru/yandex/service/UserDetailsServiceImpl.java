package ru.yandex.service;

import ru.yandex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.info("find user by username {} for authentication", username);

        return userRepository.findByUsername(username)
                .doOnSuccess(user -> {
                    if(user != null)
                        log.info("found {} for authentication", user);
                    else
                        log.info("not found user by username {}", username);
                })
                .map(user -> user);
    }
}
