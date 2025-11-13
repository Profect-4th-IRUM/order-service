package com.irum.orderservice.domain.deliveryaddress.domain.repository;

import com.irum.orderservice.domain.deliveryaddress.domain.entity.DeliveryAddress;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryAddressRepository
        extends JpaRepository<DeliveryAddress, UUID>, DeliveryAddressRepositoryCustom {

    boolean existsByMemberId(Long memberId);

    @Query("SELECT d FROM DeliveryAddress d WHERE d.memberId = :memberId AND d.isDefault = true")
    Optional<DeliveryAddress> findDefaultAddressByMemberId(@Param("memberId") Long memberId);

    Optional<DeliveryAddress> findTopByMemberIdOrderByCreatedAtDesc(Long memberId);
}
