package com.irum.orderservice.domain.order.controller;

import com.irum.orderservice.domain.order.dto.response.BalanceResponse;
import com.irum.orderservice.domain.order.dto.response.SalesResponse;
import com.irum.orderservice.domain.order.service.SalesService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class SalesController {
    private final SalesService salesService;

    //   총 주문 내역
    @GetMapping("/{storeId}/sales")
    public ResponseEntity<SalesResponse> salesResponse(@PathVariable UUID storeId) {
        SalesResponse response = salesService.getSalesList(storeId);
        return ResponseEntity.ok(response);
    }

    // 정산 내역 조회
    @GetMapping("/{storeId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable UUID storeId) {
        BalanceResponse response = salesService.getBalance(storeId);
        return ResponseEntity.ok(response);
    }
}