package com.irum.orderservice.domain.client.payment;

import com.irum.orderservice.domain.client.payment.api.PaymentAPI;
import com.irum.orderservice.domain.client.payment.dto.emuns.PaymentCorp;
import com.irum.orderservice.domain.client.payment.dto.request.CreatePaymentRequest;
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

    public UUID createPaymentPending(int finalPaymentAmount, int discountAmount, PaymentCorp paymentCorp){
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .finalPaymentAmount(finalPaymentAmount)
                .discountAmount(discountAmount)
                .paymentCorp(paymentCorp)
                .build();
        return paymentAPI.createPaymentPending(request);
    }
}
