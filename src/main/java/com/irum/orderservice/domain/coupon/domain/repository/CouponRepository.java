package com.irum.orderservice.domain.coupon.domain.repository;

import com.irum.orderservice.domain.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    List<Coupon> findByMemberId(Long memberId);

    @Query("SELECT c FROM Coupon c WHERE c.member.memberId = :memberId AND c.expiration > :now")
    List<Coupon> findValidCouponByMemberId(
            @Param("memberId") Long memberId, @Param("now") LocalDateTime now);
}
