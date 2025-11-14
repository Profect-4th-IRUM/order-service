package com.irum.orderservice.openfeign.payment.dto.response;

import java.util.Map;
import java.util.UUID;

public record PaymentMapResponse(
        Map<UUID, PaymentResponse> paymentMap
) {
    public record PaymentResponse(
            int discountAmount,
            int payingAmount
    ){

    }
}
