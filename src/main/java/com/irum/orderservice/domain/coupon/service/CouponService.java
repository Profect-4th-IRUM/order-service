package com.irum.orderservice.domain.coupon.service;

import com.irum.global.advice.exception.CommonException;
import com.irum.orderservice.domain.coupon.domain.entity.Coupon;
import com.irum.orderservice.domain.coupon.domain.repository.AppliedCouponRepository;
import com.irum.orderservice.domain.coupon.domain.repository.CouponRepository;
import com.irum.orderservice.domain.coupon.dto.request.CouponGenerateRequest;
import com.irum.orderservice.domain.coupon.dto.response.CouponResponse;
import com.irum.orderservice.global.util.MemberUtil;
import com.irum.orderservice.global.exception.errorcode.CouponErrorCode;
import com.irum.orderservice.global.exception.errorcode.MemberErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {
    private final MemberUtil memberUtil;
    private final CouponRepository couponRepository;
    private final AppliedCouponRepository appliedCouponRepository;

    public void createCoupon(CouponGenerateRequest request, Long memberId) {
        if (memberId == null) {
            throw new CommonException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        Coupon coupon =
                Coupon.createCoupon(
                        request.name(),
                        request.discountAmount(),
                        request.expiration(),
                        memberId);

        couponRepository.save(coupon);
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> getCouponByMember(Long memberId) {
        List<Coupon> coupons = couponRepository.findByMemberId(memberId);

        if (coupons.isEmpty()) {
            return List.of();
        }

        List<UUID> couponIds = coupons.stream()
                .map(Coupon::getId)
                .toList();

        List<UUID> usedCouponIds = appliedCouponRepository.findByCouponIdIn(couponIds)
                .stream()
                .map(appliedCoupon -> appliedCoupon.getCoupon().getId())
                .toList();

        return coupons.stream()
                .filter(coupon -> !usedCouponIds.contains(coupon.getId()))
                .map(CouponResponse::from)
                .toList();
    }



    public void deleteCoupon(UUID couponId, Long memberId) {
        Coupon coupon =
                couponRepository
                        .findById(couponId)
                        .orElseThrow(() -> new CommonException(CouponErrorCode.COUPON_NOT_FOUND));
        if (!coupon.getMemberId().equals(memberId)) {
            throw new CommonException(CouponErrorCode.ONLY_OWNER_CAN_DELETE);
        }
        coupon.softDelete(memberId);

    }

    /** 쿠폰 유효성 검증 및 할인 금액 계산 */
    public int validAndCalCoupon(List<UUID> couponIdList, int calculatedTotalPrice, Long memberId) {
        if (couponIdList.isEmpty()) {
            return 0;
        }

        int totalDiscount = 0;
        List<Coupon> couponList = couponRepository.findAllById(couponIdList);

        // 사용된 쿠폰 ID를 한 번에 조회 (N+1 해결)
        List<UUID> appliedCouponIds = appliedCouponRepository.findByCouponIdIn(couponIdList)
                .stream()
                .map(appliedCoupon -> appliedCoupon.getCoupon().getId())
                .toList();

        for (Coupon coupon : couponList) {
            // 권한 검사
            if (!coupon.getMemberId().equals(memberId)) {
                throw new CommonException(CouponErrorCode.COUPON_NO_PERMISSION);
            }
            // 만료일 검사
            if (coupon.getExpiration().isBefore(LocalDateTime.now())) {
                throw new CommonException(CouponErrorCode.COUPON_EXPIRATION);
            }
            // 사용 여부 검사
            if (appliedCouponIds.contains(coupon.getId())) {
                throw new CommonException(CouponErrorCode.COUPON_ALREADY_USED);
            }
            totalDiscount += coupon.getDiscountAmount();
        }

        if (totalDiscount > calculatedTotalPrice) {
            totalDiscount = calculatedTotalPrice;
        }

        return totalDiscount;
    }
}
