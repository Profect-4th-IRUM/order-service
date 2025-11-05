package com.irum.orderservice.domain.order.mapper;

import com.irum.orderservice.domain.client.payment.dto.response.PaymentResponse;
import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.dto.response.AddressResponse;
import com.irum.orderservice.domain.order.dto.response.CustomerOrderListResponse;
import com.irum.orderservice.domain.order.dto.response.CustomerOrderResponse;
import com.irum.orderservice.domain.order.dto.response.OrderDetailResponse;
import com.irum.orderservice.domain.order.repository.dto.CustomerOrderDetailRow;
import com.irum.orderservice.domain.order.repository.dto.CustomerOrderSummaryRow;
import com.irum.orderservice.domain.refund.domain.entity.Refund;
import com.irum.orderservice.domain.refund.domain.entity.enums.RefundStatus;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CustomerOrderMapper {

    public static OrderDetailResponse toOrderDetailResponse(
            Order order, List<OrderDetail> orderDetailList, Refund refund, PaymentResponse payment) {
        List<OrderDetailResponse.ProductResponse> pList =
                orderDetailList.stream().map(CustomerOrderMapper::toProductResponse).toList();

        RefundStatus refundStatus;
        if (refund == null) {
            refundStatus = null;
        } else {
            refundStatus = refund.getRefundStatus();
        }

        return OrderDetailResponse.builder()
                .orderAt(order.getCreatedAt())
                .paymentStatus(payment.paymentStatus())
                .paymentMethod(payment.paymentMethod())
                .deliveryFee(order.getDeliveryFee())
                .discountAmount(payment.totalDiscountAmount())
                .totalProductPrice(order.getTotalPrice())
                .totalPaymentPrice(payment.amount())
                .orderStatusAll(order.getOrderStatusAll())
                .refundStatus(refundStatus)
                .deliveryRequest(order.getDeliveryRequest())
                .address(AddressResponse.from(order.getDeliveryAddress().getAddress()))
                .recipientContact(order.getDeliveryAddress().getRecipientContact())
                .recipientName(order.getDeliveryAddress().getRecipientName())
                .productResponseList(pList)
                .build();
    }

    private static OrderDetailResponse.ProductResponse toProductResponse(OrderDetail orderDetail) {

        return OrderDetailResponse.ProductResponse.builder()
                .receivedDate(orderDetail.getArrivedDate().toLocalDate())
                .productName(orderDetail.getProductName())
                .optionTitle(orderDetail.getOptionName())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }

    public static CustomerOrderResponse toCustomerOrderResponse(
            Order order, List<OrderDetail> orderDetailList, int discountAmount, int finalPaymentAmount) {
        List<CustomerOrderResponse.ProductSummary> productSummaryList =
                orderDetailList.stream().map(CustomerOrderMapper::toProductSummary).toList();

        return CustomerOrderResponse.builder()
                .orderId(order.getOrderId())
                .address(AddressResponse.from(order.getDeliveryAddress().getAddress()))
                .totalProductPrice(order.getTotalPrice())
                .totalDiscountAmount(discountAmount)
                .totalPaymentAmount(finalPaymentAmount)
                .productList(productSummaryList)
                .build();
    }

    private static CustomerOrderResponse.ProductSummary toProductSummary(OrderDetail orderDetail) {
        return CustomerOrderResponse.ProductSummary.builder()
                .orderDetailId(orderDetail.getOrderDetailId())
                .productName(orderDetail.getProductName())
                .optionName(orderDetail.getOptionName())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }

    public static CustomerOrderListResponse.OrderResponse toOrderResponse(
            CustomerOrderSummaryRow or, List<CustomerOrderListResponse.ProductResponse> pList) {

        return CustomerOrderListResponse.OrderResponse.builder()
                .orderId(or.orderId())
                .orderAt(or.orderAt())
                .refundStatus(or.refundStatus())
                .productResponseList(pList)
                .build();
    }

    public static CustomerOrderListResponse.ProductResponse toProductResponse(
            CustomerOrderDetailRow c) {
        return CustomerOrderListResponse.ProductResponse.builder()
                .orderStatus(c.orderStatusIndi())
                .orderDetailId(c.orderDetailId())
                .optionName(c.optionName())
                .price(c.price())
                .productName(c.productName())
                .quantity(c.quantity())
                .build();
    }
}
