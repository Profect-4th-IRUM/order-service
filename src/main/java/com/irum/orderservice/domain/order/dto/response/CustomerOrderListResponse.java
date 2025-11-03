package com.irum.orderservice.domain.order.dto.response;

import com.irum.orderservice.domain.order.domain.entity.enums.OrderStatus;
import com.irum.orderservice.domain.refund.domain.entity.enums.RefundStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CustomerOrderListResponse(
        UUID nextCursor, Boolean hasNext, List<OrderResponse> orderList) {
    @Builder
    public record OrderResponse(
            UUID orderId,
            LocalDateTime orderAt,
            RefundStatus refundStatus,
            List<ProductResponse> productResponseList) {}

    @Builder
    public record ProductResponse(
            UUID orderDetailId,
            String productName,
            String optionName,
            int quantity,
            int price,
            OrderStatus orderStatus) {}
}
