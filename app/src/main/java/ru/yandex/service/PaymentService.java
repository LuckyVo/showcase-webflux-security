package ru.yandex.service;

import ru.yandex.pay.dto.BalanceResponse;
import ru.yandex.pay.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<ResponseEntity<BalanceResponse>> getBalance();

    Mono<ResponseEntity<PaymentResponse>> payment(double amount);
}
