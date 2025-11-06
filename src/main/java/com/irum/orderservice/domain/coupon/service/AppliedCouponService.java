package com.irum.orderservice.domain.coupon.service;

import com.irum.orderservice.domain.coupon.domain.entity.AppliedCoupon;
import com.irum.orderservice.domain.coupon.domain.entity.Coupon;
import com.irum.orderservice.global.util.MemberUtil;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppliedCouponService {
    private final com.irum.orderservice.domain.coupon.repository.AppliedCouponRepository
            appliedCouponRepository;
    private final com.irum.orderservice.domain.coupon.repository.CouponRepository couponRepository;
    private final MemberUtil memberUtil;

    /** 쿠폰 사용 처리 */
    public void createAppliedCouponList(Payment payment, List<UUID> couponIdList) {
        List<Coupon> couponList = couponRepository.findAllById(couponIdList);

        List<AppliedCoupon> appliedCouponList =
                couponList.stream()
                        .map(
                                coupon ->
                                        AppliedCoupon.builder()
                                                .payment(payment)
                                                .coupon(coupon)
                                                .build())
                        .toList();

        appliedCouponRepository.saveAll(appliedCouponList);
    }

    /** 롤백 */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void rollbackAppliedCouponList(UUID paymentId) {
        appliedCouponRepository.deleteByPaymentId(paymentId);
    }
}
