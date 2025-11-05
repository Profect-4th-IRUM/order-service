package com.irum.orderservice.domain.deliveryaddress.domain.repository;


import com.irum.orderservice.domain.deliveryaddress.domain.entity.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryAddressRepository
        extends JpaRepository<DeliveryAddress, UUID>,
                com.irum.orderservice.domain.deliveryaddress.domain.repository
                        .DeliveryAddressRepositoryCustom {

    boolean existsByMember(Long member);

    @Query("SELECT d FROM DeliveryAddress d WHERE d.member = :member AND d.isDefault = true")
    Optional<DeliveryAddress> findDefaultAddressByMember(@Param("member") Long member);

    Optional<DeliveryAddress> findTopByMemberOrderByCreatedAtDesc(Long member);
}
