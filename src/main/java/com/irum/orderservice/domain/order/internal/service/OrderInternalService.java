package com.irum.orderservice.domain.order.internal.service;

import com.irum.global.advice.exception.CommonException;
import com.irum.openfeign.order.dto.request.UpdateOrderStatusFailedRequest;
import com.irum.openfeign.order.dto.request.UpdateOrderStatusPreparingRequest;
import com.irum.openfeign.product.client.ProductClient;
import com.irum.openfeign.product.dto.request.RollbackStockRequest;
import com.irum.orderservice.domain.coupon.service.AppliedCouponService;
import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.domain.entity.enums.OrderStatus;
import com.irum.orderservice.domain.order.domain.repository.OrderDetailRepository;
import com.irum.orderservice.domain.order.domain.repository.OrderRepository;
import com.irum.orderservice.domain.order.event.PaymentFailedEvent;
import com.irum.orderservice.domain.order.event.PaymentPaidEvent;
import com.irum.orderservice.domain.order.producer.OrderEventProducer;
import com.irum.orderservice.global.exception.errorcode.OrderErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderInternalService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AppliedCouponService appliedCouponService;
    private final ProductClient productClient;

    private final OrderEventProducer orderEventProducer;

    /** EDA - 주문 및 주문 상세 상태 변경 - preparing */
    public void updateOrderStatusPreparing(PaymentPaidEvent event) {
        Order order =
                orderRepository
                        .findByOrderId(event.orderId())
                        .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));
        log.info("[DB] order 조회 완료 {}", order.getOrderId());

        order.updateOrderStatus(OrderStatus.PREPARING);
        orderRepository.flush();
        log.info("[DB] order 업데이트 완료 {}", order.getOrderId());

        orderDetailRepository.updateStatusToPreparingByOrderId(event.orderId());
        log.info("[DB] order detail 업데이트 완료");
    }

    /** REST API - 주문 및 주문 상세 상태 변경 - preparing */
    public String updateOrderStatusPreparing(UpdateOrderStatusPreparingRequest request) {
        Order order =
                orderRepository
                        .findByOrderId(request.orderId())
                        .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));
        order.updateOrderStatus(OrderStatus.PREPARING);
        orderRepository.flush();
        orderDetailRepository.updateStatusToPreparingByOrderId(request.orderId());
        return order.getOrderNum();
    }

    /** EDA - 주문 및 주문 상세 상태 변경 - failed, 쿠폰 재고 롤백 */
    public void updateOrderStatusFailed(PaymentFailedEvent event) {
        Order order =
                orderRepository
                        .findByOrderId(event.orderId())
                        .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));
        order.updateOrderStatus(OrderStatus.FAILED);
        orderRepository.flush();
        orderDetailRepository.updateStatusToFailedByOrderId(event.orderId());

        // 쿠폰 롤백
        appliedCouponService.rollbackAppliedCouponList(event.paymentId());

        // 재고 롤백
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrder(order);
        orderEventProducer.sendOrderFailedEvent(orderDetailList, order.getOrderId());
        //
        //        List<RollbackStockRequest.OptionValueRequest> optionValueRequestList =
        //                orderDetailList.stream()
        //                        .map(
        //                                o ->
        //                                        RollbackStockRequest.OptionValueRequest.builder()
        //                                                .optionValueId(o.getOptionValueId())
        //                                                .quantity(o.getQuantity())
        //                                                .build())
        //                        .toList();
        //
        //        RollbackStockRequest rollbackStockRequest =
        //
        // RollbackStockRequest.builder().optionValueList(optionValueRequestList).build();
        //        productClient.rollbackStock(rollbackStockRequest);
    }

    /** REST API - 주문 및 주문 상세 상태 변경 - failed, 쿠폰 재고 롤백 */
    public void updateOrderStatusFailed(UpdateOrderStatusFailedRequest request) {
        Order order =
                orderRepository
                        .findByOrderId(request.orderId())
                        .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));
        order.updateOrderStatus(OrderStatus.FAILED);
        orderRepository.flush();
        orderDetailRepository.updateStatusToFailedByOrderId(request.orderId());

        // 쿠폰 롤백
        appliedCouponService.rollbackAppliedCouponList(request.paymentId());

        // 재고 롤백
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrder(order);

        List<RollbackStockRequest.OptionValueRequest> optionValueRequestList =
                orderDetailList.stream()
                        .map(
                                o ->
                                        RollbackStockRequest.OptionValueRequest.builder()
                                                .optionValueId(o.getOptionValueId())
                                                .quantity(o.getQuantity())
                                                .build())
                        .toList();

        RollbackStockRequest rollbackStockRequest =
                RollbackStockRequest.builder().optionValueList(optionValueRequestList).build();
        productClient.rollbackStock(rollbackStockRequest);
    }
}
