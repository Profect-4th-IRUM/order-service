package com.irum.orderservice.domain.coupon.event;

import java.util.List;
import java.util.UUID;

public record CouponValidatedEvent(UUID orderId, List<UUID> couponIdList) {}
