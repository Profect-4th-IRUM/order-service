package com.irum.orderservice.domain.order.internal.controller;

import com.irum.openfeign.order.dto.request.UpdateOrderStatusFailedRequest;
import com.irum.openfeign.order.dto.request.UpdateOrderStatusPreparingRequest;
import com.irum.orderservice.domain.order.internal.dto.request.UpdateOrderFailedRequest;
import com.irum.orderservice.domain.order.internal.service.OrderInternalService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/orders")
public class OrderInternalController {
    private final OrderInternalService orderInternalService;

    @PatchMapping("/preparing")
    public void updateOrderStatusPreparing(@RequestBody UpdateOrderStatusPreparingRequest request) {
        orderInternalService.updateOrderStatusPreparing(request);
    }

    @PatchMapping("/failed")
    public void updateOrderStatusFailed(
            @RequestBody UpdateOrderStatusFailedRequest request) {
        orderInternalService.updateOrderStatusFailed(request);
    }
}
