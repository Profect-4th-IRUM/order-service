package com.irum.orderservice.domain.order.internal.service;

import com.irum.global.advice.exception.CommonException;
import com.irum.orderservice.domain.client.product.ProductClient;
import com.irum.orderservice.domain.coupon.service.AppliedCouponService;
import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.domain.entity.enums.OrderStatus;
import com.irum.orderservice.domain.order.domain.repository.OrderDetailRepository;
import com.irum.orderservice.domain.order.domain.repository.OrderRepository;
import com.irum.orderservice.domain.order.internal.dto.request.UpdateOrderFailedRequest;
import java.util.List;
import java.util.UUID;

import com.irum.orderservice.global.exception.errorcode.OrderErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderInternalService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AppliedCouponService appliedCouponService;
    private final ProductClient productClient;

    /** 주문 및 주문 상세 상태 변경 - preparing */
    public void updateOrderStatusPreparing(UUID orderId) {
        Order order =
                orderRepository
                        .findByOrderId(orderId)
                        .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));
        order.updateOrderStatus(OrderStatus.PREPARING);

        orderDetailRepository.updateStatusToPreparingByOrderId(orderId);
    }

    /** 주문 및 주문 상세 상태 변경 - failed, 쿠폰 재고 롤백 */
    public void updateOrderStatusFailed(UUID orderId, UpdateOrderFailedRequest request) {
        Order order =
                orderRepository
                        .findByOrderId(orderId)
                        .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));
        order.updateOrderStatus(OrderStatus.FAILED);

        orderDetailRepository.updateStatusToPreparingByOrderId(orderId);

        // 쿠폰 롤백
        appliedCouponService.rollbackAppliedCouponList(request.paymentId());

        // 재고 롤백
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrder(order);
        productClient.rollbackStock(orderDetailList);
    }
}
