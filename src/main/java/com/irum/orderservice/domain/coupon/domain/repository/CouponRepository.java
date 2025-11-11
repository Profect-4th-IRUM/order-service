package com.irum.orderservice.domain.coupon.domain.repository;

import com.irum.orderservice.domain.coupon.domain.entity.Coupon;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    List<Coupon> findByMemberId(Long memberId);
}
