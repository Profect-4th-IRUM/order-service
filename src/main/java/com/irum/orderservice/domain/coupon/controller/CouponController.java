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
        Long memberId = memberUtil.getCurrentMember().getMemberId();
        couponService.createCoupon(request, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getCoupon() {
        Long memberId = memberUtil.getCurrentMember().getMemberId();
        return ResponseEntity.ok(couponService.getCouponByMember(memberId));
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable("couponId") UUID couponId) {
        Long memberId = memberUtil.getCurrentMember().getMemberId();
        couponService.deleteCoupon(couponId, memberId);
        return ResponseEntity.noContent().build();
    }
}
