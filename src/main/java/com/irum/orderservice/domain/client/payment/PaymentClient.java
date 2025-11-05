package com.irum.orderservice.domain.client.payment;

import com.irum.orderservice.domain.client.payment.api.PaymentAPI;
import com.irum.orderservice.domain.client.payment.dto.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentClient {
    private final PaymentAPI paymentAPI;

    public PaymentResponse getPayment(UUID paymentId) {
        return paymentAPI.getPayment(paymentId);
    }
}
