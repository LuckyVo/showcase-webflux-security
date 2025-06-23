package ru.yandex.repository;

import ru.yandex.entity.Order;
import ru.yandex.utils.Status;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {

    Flux<Order> findByUserIdAndStatus(long userId, Status status);
}
