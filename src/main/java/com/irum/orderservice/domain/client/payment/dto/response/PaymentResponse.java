package com.irum.orderservice.domain.client.payment.dto.response;

import com.irum.orderservice.domain.client.payment.dto.emuns.PaymentMethod;
import com.irum.orderservice.domain.client.payment.dto.emuns.PaymentStatus;

public record PaymentResponse (
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        int totalDiscountAmount,
        int amount
){
}
