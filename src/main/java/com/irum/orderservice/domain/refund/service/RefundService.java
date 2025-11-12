package com.irum.orderservice.domain.refund.service;

import com.irum.global.advice.exception.CommonException;
import com.irum.orderservice.domain.client.payment.PaymentClient;
import com.irum.orderservice.domain.client.product.ProductClient;
import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.domain.entity.enums.OrderStatus;
import com.irum.orderservice.domain.order.domain.repository.OrderDetailRepository;
import com.irum.orderservice.domain.order.domain.repository.OrderRepository;
import com.irum.orderservice.domain.refund.domain.entity.Refund;
import com.irum.orderservice.domain.refund.domain.entity.enums.RefundStatus;
import com.irum.orderservice.domain.refund.domain.repository.RefundRepository;
import com.irum.orderservice.domain.refund.dto.request.RefundCreateRequest;
import com.irum.orderservice.domain.refund.dto.request.StoreRefundStatusRequest;
import com.irum.orderservice.domain.refund.dto.response.RefundDetailResponse;
import com.irum.orderservice.domain.refund.dto.response.RefundOrderList;
import com.irum.orderservice.domain.refund.dto.response.RefundProductList;
import com.irum.orderservice.domain.refund.dto.response.StoreRefundListResponse;
import com.irum.orderservice.global.exception.errorcode.OrderErrorCode;
import com.irum.orderservice.global.exception.errorcode.RefundErrorCode;
import com.irum.orderservice.global.util.MemberUtil;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefundService {

    private final MemberUtil memberUtil;
    private final RefundRepository refundRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentClient paymentClient;

    // Customer
    public void createRefund(UUID orderId, RefundCreateRequest request) {
        assertNoRefundExistsByOrder(orderId);
        Order order = getValidOrder(orderId);
        // 환불 가능한 주문 확인
        if (!isRefundableOrderStatus(order.getOrderStatusAll()))
            throw new CommonException(RefundErrorCode.REFUND_NOT_AVAILABLE);

        int refundAmount = paymentClient.getPaymentAmount(order.getPaymentId());

        refundRepository.save(Refund.create(request.reason(), request.description(), order, refundAmount));
    }

    @Transactional(readOnly = true)
    public RefundDetailResponse findRefundDetail(UUID orderId) {
        Order order = getValidOrderWithAddressAndPayment(orderId);
        Refund refund = getValidRefund(orderId);
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrder(order);
        return RefundDetailResponse.of(order, orderDetails, refund);
    }

    // Owner
    @Transactional(readOnly = true)
    public StoreRefundListResponse findRefundListByStatus(RefundStatus status) {
        List<Refund> refunds = refundRepository.findByRefundStatus(status);

        var orderList =
                refunds.stream()
                        .map(
                                refund -> {
                                    var order = refund.getOrder();
                                    var products = List.<RefundProductList>of();

                                    return new RefundOrderList(
                                            order.getOrderId(),
                                            order.getDeliveryAddress().getRecipientName(),
                                            order.getCreatedAt(),
                                            refund.getCreatedAt(),
                                            refund.getRefundStatus(),
                                            refund.getPrice(),
                                            products);
                                })
                        .toList();

        int total = refunds.stream().mapToInt(Refund::getPrice).sum();

        return new StoreRefundListResponse(orderList, String.valueOf(total), false, null);
    }

    public void changeRefundStatus(UUID refundId, StoreRefundStatusRequest request) {
        Refund refund =
                refundRepository
                        .findById(refundId)
                        .orElseThrow(() -> new CommonException(RefundErrorCode.REFUND_NOT_FOUND));

        RefundStatus currentStatus = refund.getRefundStatus();
        RefundStatus newStatus = request.refundStatus();

        validateRefundStatusTransition(currentStatus, newStatus);

        refund.updateStatus(request.refundStatus());

        Order order = refund.getOrder();
        updateOrderStatusByRefundStatus(order, newStatus);
    }

    // 확인 가능한 주문 상태 확인 (OrderStatus가 PREPARING일때만 가능)
    private boolean isRefundableOrderStatus(OrderStatus orderStatus) {
        return orderStatus == OrderStatus.PREPARING;
    }

    // 환불 상태 변경 제약 (단계별 수정만 가능)
    private void validateRefundStatusTransition(RefundStatus current, RefundStatus next) {
        // 초기값인 PENDING 상태에서만 APPROVED or REJECTED 로 변경 가능
        if (current == RefundStatus.PENDING) {
            if (next != RefundStatus.APPROVED && next != RefundStatus.REJECTED) {
                throw new CommonException(RefundErrorCode.INVALID_STATUS_TRANSITION);
            }
            return;
        }
        if (current == RefundStatus.APPROVED) {
            if (next != RefundStatus.COMPLETED) {
                throw new CommonException(RefundErrorCode.INVALID_STATUS_TRANSITION);
            }
            return;
        }
        // 그 외 상태에서는 전환 불가
        throw new CommonException(RefundErrorCode.INVALID_STATUS_TRANSITION);
    }

    // 환불 상태 변경에 따른 주문 상태 업데이트
    private void updateOrderStatusByRefundStatus(Order order, RefundStatus refundStatus) {
        switch (refundStatus) {
            case APPROVED:
                order.updateOrderStatus(OrderStatus.FAILED);
                break;
            case REJECTED:
                order.updateOrderStatus(OrderStatus.PREPARING);
                break;
        }
    }

    private void assertNoRefundExistsByOrder(UUID orderId) {
        if (refundRepository.existsByOrderId(orderId))
            throw new CommonException(RefundErrorCode.REFUND_ALREADY_EXISTS);
    }

    private Order getValidOrder(UUID orderId) {
        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));
        memberUtil.assertMemberResourceAccess(order.getMemberId());
        return order;
    }

    private Order getValidOrderWithAddressAndPayment(UUID orderId) {
        Order order =
                orderRepository
                        .findOrderWithAddressAndPayment(orderId)
                        .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));
        memberUtil.assertMemberResourceAccess(order.getMemberId());
        return order;
    }

    private Refund getValidRefund(UUID orderId) {
        return refundRepository
                .findByOrderId(orderId)
                .orElseThrow(() -> new CommonException(RefundErrorCode.REFUND_NOT_FOUND));
    }
}
