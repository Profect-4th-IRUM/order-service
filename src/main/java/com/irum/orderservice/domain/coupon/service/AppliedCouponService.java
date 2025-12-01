package com.irum.orderservice.domain.coupon.service;

import com.irum.orderservice.domain.coupon.domain.entity.AppliedCoupon;
import com.irum.orderservice.domain.coupon.domain.entity.Coupon;
import com.irum.orderservice.domain.coupon.domain.repository.AppliedCouponRepository;
import com.irum.orderservice.domain.coupon.domain.repository.CouponRepository;
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
    private final AppliedCouponRepository appliedCouponRepository;
    private final CouponRepository couponRepository;
    private final MemberUtil memberUtil;

    /** 쿠폰 사용 처리 */
    public void createAppliedCouponList(UUID paymentId, List<UUID> couponIdList) {
        List<Coupon> couponList = couponRepository.findAllById(couponIdList);

        List<AppliedCoupon> appliedCouponList =
                couponList.stream()
                        .map(
                                coupon ->
                                        AppliedCoupon.builder()
                                                .paymentId(paymentId)
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

    /** 롤백 */
    @Transactional
    public void rollbackAppliedCouponList(List<UUID> paymentIdList) {
        appliedCouponRepository.deleteAllByPaymentIds(paymentIdList);
    }
}
