package com.irum.orderservice.domain.coupon.domain.repository;

import com.irum.orderservice.domain.coupon.domain.entity.AppliedCoupon;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppliedCouponRepository extends JpaRepository<AppliedCoupon, UUID> {
    List<AppliedCoupon> findByCouponId(UUID couponId);

    boolean existsByCouponId(UUID couponId);

    void deleteByPaymentId(UUID paymentId);

    List<AppliedCoupon> findByPayment_PaymentId(UUID paymentId);
}
