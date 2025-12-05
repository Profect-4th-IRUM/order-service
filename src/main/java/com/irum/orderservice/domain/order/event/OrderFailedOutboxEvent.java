package com.irum.orderservice.domain.order.event;

import java.util.UUID;

public record OrderFailedOutboxEvent (
        UUID orderId,
        OrderFailedEvent payload
){
}
