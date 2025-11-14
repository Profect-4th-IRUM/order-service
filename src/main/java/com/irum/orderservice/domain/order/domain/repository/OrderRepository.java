package com.irum.orderservice.domain.order.domain.repository;

import com.irum.orderservice.domain.order.domain.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {

    Optional<Order> findByOrderIdAndMemberId(UUID orderId, Long memberId);

    List<Order> findAllByMemberId(Long member);

    List<Order> findAllByStoreId(UUID storeId);

    Optional<Order> findByOrderId(UUID orderId);

    @Query(
            """
    SELECT o FROM Order o
    WHERE o.orderStatusAll = 'PENDING' AND o.createdAt < :cutoffTime
    """)
    List<Order> findStalePendingOrders(@Param("cutoffTime") LocalDateTime cutoffTime);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Order o SET o.orderStatusAll = 'FAILED' WHERE o.orderId IN :orderIds")
    int updateStatusToFailedByIds(@Param("orderIds") List<UUID> orderIds);

    @Query("SELECT o FROM Order o JOIN FETCH o.deliveryAddress da WHERE o.orderId = :orderId")
    Optional<Order> findOrderWithAddress(@Param("orderId") UUID orderId);
}
