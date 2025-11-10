package com.irum.orderservice.domain.order.internal.dto.response;

import java.util.UUID;

public record OrderDetailInternalResponse(
        UUID orderDetailId,
        UUID productId,
        Long memberId,
        String orderStatus
) {}
