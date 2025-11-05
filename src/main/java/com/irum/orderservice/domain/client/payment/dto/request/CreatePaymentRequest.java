package com.irum.orderservice.domain.client.payment.dto.request;

import com.irum.orderservice.domain.client.payment.dto.emuns.PaymentCorp;
import lombok.Builder;

@Builder
public record CreatePaymentRequest(
        int finalPaymentAmount, int discountAmount, PaymentCorp paymentCorp
) {
}