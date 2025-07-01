package ru.yandex.service;

import ru.yandex.pay.dto.BalanceResponse;
import ru.yandex.pay.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<ResponseEntity<BalanceResponse>> getBalance(long userId);

    Mono<ResponseEntity<PaymentResponse>> payment(long userId, double amount);
}
