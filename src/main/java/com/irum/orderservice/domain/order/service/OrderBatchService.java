package com.irum.orderservice.domain.order.service;

import com.irum.orderservice.domain.client.payment.PaymentClient;
import com.irum.orderservice.domain.client.product.ProductClient;
import com.irum.orderservice.domain.coupon.service.AppliedCouponService;
import com.irum.orderservice.domain.coupon.service.CouponService;
import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.domain.repository.OrderDetailRepository;
import com.irum.orderservice.domain.order.domain.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderBatchService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentClient paymentClient;
    private final ProductClient productClient;
    private final AppliedCouponService appliedCouponService;


    private static final int TIMEOUT_MINUTES = 5; // 5분 기준

    public void processStalePendingOrders() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(TIMEOUT_MINUTES);

        // 타임아웃된 주문 조회
        List<Order> staleOrders = orderRepository.findStalePendingOrders(cutoffTime);

        if (staleOrders.isEmpty()) {
            return; // 처리할 주문 없음
        }

        // 대상 ID 수집
        List<UUID> orderIds = staleOrders.stream().map(Order::getOrderId).toList();

        List<UUID> paymentIds =
                staleOrders.stream().map(Order::getPaymentId).toList();

        // OrderDetail 상태 변경
        int detailCount = orderDetailRepository.updateStatusToFailedByOrderIds(orderIds);
        // Order 상태 변경
        int orderCount = orderRepository.updateStatusToFailedByIds(orderIds);


        //쿠폰 롤백
        appliedCouponService.rollbackAppliedCouponList(paymentIds);

        //OrderDetail 조회
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderIds(orderIds);

        // Payment 상태 변경
        int paymentCount = paymentClient.updateStatusToFailed(paymentIds);
        // 재고 롤백
        productClient.rollbackStock(orderDetailList);


        log.info(
                "[주문 타임아웃 배치] {}개 주문, {}개 결제, {}개 주문상세 'FAILED' 처리 완료",
                orderCount,
                paymentCount,
                detailCount);
    }
}
