package com.irum.orderservice.domain.coupon.controller;

import com.irum.orderservice.domain.coupon.dto.request.CouponGenerateRequest;
import com.irum.orderservice.domain.coupon.dto.response.CouponResponse;
import com.irum.orderservice.domain.coupon.service.CouponService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Void> createCoupon(@Valid @RequestBody CouponGenerateRequest request) {
        couponService.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getCoupon() {
        return ResponseEntity.ok(couponService.getCouponByMember());
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable("couponId") UUID couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}
