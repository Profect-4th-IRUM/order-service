package com.irum.orderservice.openfeign.payment.dto.request;

import com.irum.orderservice.openfeign.payment.dto.emuns.PaymentCorp;
import lombok.Builder;

@Builder
public record CreatePaymentRequest(
        int finalPaymentAmount, int discountAmount, PaymentCorp paymentCorp) {}
