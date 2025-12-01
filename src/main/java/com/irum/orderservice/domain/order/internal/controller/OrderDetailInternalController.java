package com.irum.orderservice.domain.order.internal.controller;

import com.irum.orderservice.domain.order.internal.dto.response.OrderDetailInternalResponse;
import com.irum.orderservice.domain.order.internal.service.OrderDetailInternalService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/order-details")
public class OrderDetailInternalController {

    private final OrderDetailInternalService orderDetailInternalService;

    @GetMapping("/{orderDetailId}")
    public OrderDetailInternalResponse getOrderDetail(@PathVariable UUID orderDetailId) {
        return orderDetailInternalService.getOrderDetail(orderDetailId);
    }
}
