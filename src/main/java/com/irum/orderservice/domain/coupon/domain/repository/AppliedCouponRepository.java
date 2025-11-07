package com.irum.orderservice.domain.coupon.domain.repository;

import com.irum.orderservice.domain.coupon.domain.entity.AppliedCoupon;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppliedCouponRepository extends JpaRepository<AppliedCoupon, UUID> {
    List<AppliedCoupon> findByCouponId(UUID couponId);

    boolean existsByCouponId(UUID couponId);

    void deleteByPayment(Payment payment);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM AppliedCoupon ac WHERE ac.paymentId IN :paymentIds")
    void deleteAllByPaymentIds(List<UUID> paymentIds);

    List<AppliedCoupon> findByPayment_PaymentId(UUID paymentId);
}
