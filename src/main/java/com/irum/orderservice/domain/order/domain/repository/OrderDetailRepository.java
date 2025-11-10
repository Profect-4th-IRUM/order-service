package com.irum.orderservice.domain.order.domain.repository;

import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

    /**
     * 주문 상세 단건 조회
     */
    Optional<OrderDetail> findByOrderDetailId(UUID orderDetailId);

    /**
     * 주문과 함께 주문 상세 조회
     */
    @Query("""
       SELECT od 
       FROM OrderDetail od
       JOIN FETCH od.order o
       WHERE od.orderDetailId = :orderDetailId
    """)
    Optional<OrderDetail> findByOrderDetailIdWithOrder(@Param("orderDetailId") UUID orderDetailId);

    /**
     * 특정 주문에 포함된 모든 주문 상세 조회
     */
    List<OrderDetail> findAllByOrder(Order order);

    /**
     * 주문 상태를 FAILED로 일괄 변경
     */
    @Modifying(clearAutomatically = true)
    @Query("""
        UPDATE OrderDetail od 
        SET od.orderStatusIndi = 'FAILED' 
        WHERE od.order.orderId IN :orderIds
    """)
    int updateStatusToFailedByOrderIds(@Param("orderIds") List<UUID> orderIds);
}
