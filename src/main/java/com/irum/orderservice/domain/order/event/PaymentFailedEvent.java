package com.irum.orderservice.domain.order.event;


import com.irum.openfeign.order.enums.OrderStatus;

import java.util.UUID;

public record PaymentFailedEvent (
        UUID orderId, UUID paymentId, OrderStatus orderStatus
){
}