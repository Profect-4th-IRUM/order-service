package com.irum.orderservice.domain.order.event;

import java.util.List;
import java.util.UUID;

public record CouponValidatedEvent(UUID orderId, List<UUID> couponIdList) {}
