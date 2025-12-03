package com.irum.orderservice.domain.order.event;

import java.util.List;
import java.util.UUID;

public record CouponAppliedEvent(UUID paymentId, List<UUID> couponIdList) {}
