package com.irum.orderservice.domain.order.internal.controller;

import com.irum.orderservice.domain.order.internal.dto.request.UpdateOrderFailedRequest;
import com.irum.orderservice.domain.order.mapper.OrderInternalService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("internal/orders/")
public class OrderInternalController {
    private final OrderInternalService orderInternalService;

    @PatchMapping("/{orderId}/preparing")
    public void updateOrderStatusPreparing(@PathVariable UUID orderId) {
        orderInternalService.updateOrderStatusPreparing(orderId);
    }

    @PatchMapping("/{orderId}/failed")
    public void updateOrderStatusFailed(
            @PathVariable UUID orderId, @RequestBody UpdateOrderFailedRequest request) {
        orderInternalService.updateOrderStatusFailed(orderId, request);
    }
}
