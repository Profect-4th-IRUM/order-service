package com.irum.orderservice.domain.coupon.event;

import java.util.UUID;

public record CouponRollbackEvent(UUID paymentId) {}
