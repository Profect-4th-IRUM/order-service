package com.irum.orderservice.domain.order.internal.dto.request;

import java.util.UUID;

public record UpdateOrderFailedRequest(UUID paymentId) {}
