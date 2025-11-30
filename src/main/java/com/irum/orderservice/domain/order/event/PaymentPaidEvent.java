package com.irum.orderservice.domain.order.event;

import com.irum.openfeign.order.enums.OrderStatus;

import java.util.UUID;

public record PaymentPaidEvent (
        OrderStatus orderStatus,
        UUID orderId
){
}
