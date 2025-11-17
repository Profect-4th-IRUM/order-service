package com.irum.orderservice.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.irum.orderservice.domain.deliveryaddress.domain.entity.Address;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OwnerOrderListResponse(
        List<OrderSummary> orderList, UUID nextCursor, boolean hasNext) {
    public record OrderSummary(
            UUID orderId,
            String recipientName,
            String recipientContact,
            AddressResponse recipientAddress,
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime orderDate,
            int totalProductPrice,
            int discountAmount,
            int payingAmount,
            int deliveryFee,
            List<ProductSummary> productList) {}

    public record ProductSummary(
            UUID orderDetailId,
            String productName,
            int productCounts,
            int productPrice,
            String optionTitle) {}
}
