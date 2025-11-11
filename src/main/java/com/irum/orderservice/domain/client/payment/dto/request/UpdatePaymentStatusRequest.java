package com.irum.orderservice.domain.client.payment.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UpdatePaymentStatusRequest(List<UUID> paymentIdList) {}
