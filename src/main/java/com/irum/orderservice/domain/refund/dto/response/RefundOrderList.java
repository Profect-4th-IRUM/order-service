package com.irum.orderservice.domain.refund.dto.response;

import com.irum.orderservice.domain.refund.domain.entity.enums.RefundStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RefundOrderList(
        UUID orderId,
        String recipientName,
        LocalDateTime orderDate,
        LocalDateTime cancelDate,
        RefundStatus refundStatus,
        Integer refundPrice,
        List<RefundProductList> productLists) {}
