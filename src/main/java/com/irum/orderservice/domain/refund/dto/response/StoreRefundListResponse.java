package com.irum.orderservice.domain.refund.dto.response;

import java.util.List;

public record StoreRefundListResponse(
        List<RefundOrderList> refundOrders,
        String totalRefundPrice,
        Boolean hasNext,
        String nextCursor) {}
