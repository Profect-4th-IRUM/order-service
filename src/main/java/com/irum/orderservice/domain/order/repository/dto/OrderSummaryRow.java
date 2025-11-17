package com.irum.orderservice.domain.order.repository.dto;

import com.irum.orderservice.domain.deliveryaddress.domain.entity.Address;
import com.irum.orderservice.domain.order.dto.response.AddressResponse;

import java.time.LocalDateTime;
import java.util.UUID;

/** 내부용 주문 헤더 DTO (productList 없는 버전) */
public record OrderSummaryRow(
        UUID orderId,
        String recipientName,
        String recipientContact,
        Address recipientAddress,
        LocalDateTime orderDate,
        int totalProductPrice,
        int discountAmount,
        int payingAmount,
        int deliveryFee) {}
