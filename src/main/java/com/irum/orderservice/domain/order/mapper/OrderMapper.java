package com.irum.orderservice.domain.order.mapper;

import com.irum.orderservice.domain.order.dto.response.OwnerOrderListResponse;
import com.irum.orderservice.domain.order.repository.dto.OrderDetailRow;
import com.irum.orderservice.domain.order.repository.dto.OrderSummaryRow;
import java.util.List;

import com.irum.orderservice.openfeign.payment.dto.response.PaymentMapResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    // TODO: builder로 리팩터링
    public OwnerOrderListResponse.ProductSummary toProductSummary(OrderDetailRow detail) {
        return new OwnerOrderListResponse.ProductSummary(
                detail.orderDetailId(),
                detail.productName(),
                detail.productCounts(),
                detail.productPrice(),
                detail.optionTitle());
    }

    public OwnerOrderListResponse.OrderSummary toOrderSummary(
            OrderSummaryRow header, List<OwnerOrderListResponse.ProductSummary> products) {
        return new OwnerOrderListResponse.OrderSummary(
                header.orderId(),
                header.recipientName(),
                header.recipientContact(),
                header.recipientAddress(),
                header.orderDate(),
                header.totalProductPrice(),
                header.discountAmount(),
                header.payingAmount(),
                header.deliveryFee(),
                products // 이미 변환된 ProductSummary 리스트를 받음
                );
    }
}
