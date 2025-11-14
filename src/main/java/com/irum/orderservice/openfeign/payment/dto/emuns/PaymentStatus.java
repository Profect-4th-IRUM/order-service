package com.irum.orderservice.openfeign.payment.dto.emuns;

public enum PaymentStatus {
    FAILED,
    PENDING,
    APPROVED,
    PAID,
    REJECTED,
    CANCELED
}
