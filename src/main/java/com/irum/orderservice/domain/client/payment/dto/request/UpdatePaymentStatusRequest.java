package com.irum.orderservice.domain.client.payment.dto.request;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UpdatePaymentStatusRequest(
        List<UUID> paymentIdList
) {
}
