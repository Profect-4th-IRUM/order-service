package com.irum.orderservice.openfeign.payment.dto.response;

import com.irum.orderservice.openfeign.payment.dto.emuns.PaymentMethod;
import com.irum.orderservice.openfeign.payment.dto.emuns.PaymentStatus;

public record PaymentResponse(
        PaymentStatus paymentStatus,
        PaymentMethod paymentMethod,
        int totalDiscountAmount,
        int amount
) {}
