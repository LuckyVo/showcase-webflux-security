package ru.yandex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;

@SpringBootApplication(exclude = RedisReactiveAutoConfiguration.class)
public class ShowCaseWebFluxSecurity {
    public static void main(String[] args) {
        SpringApplication.run(ShowCaseWebFluxSecurity.class, args);
    }
}
