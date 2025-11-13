package com.irum.orderservice.domain.order.service;

import com.irum.orderservice.domain.client.store.StoreClient;
import com.irum.orderservice.domain.client.store.dto.response.StoreResponse;
import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.repository.OrderRepository;
import com.irum.orderservice.domain.order.dto.response.BalanceResponse;
import com.irum.orderservice.domain.order.dto.response.SalesResponse;
import com.irum.orderservice.domain.refund.domain.entity.Refund;
import com.irum.orderservice.domain.refund.domain.entity.enums.RefundStatus;
import com.irum.orderservice.domain.refund.domain.repository.RefundRepository;
import com.irum.orderservice.global.util.MemberUtil;

import java.sql.Ref;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalesService {
    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;
    private final MemberUtil memberUtil;
    private final StoreClient storeClient;

    public SalesResponse getSalesList(UUID storeId) {

        StoreResponse storeResponse = storeClient.getStoreInfo(storeId);
        Long CurrentMemberId = memberUtil.getCurrentMember().memberId();
        memberUtil.assertMemberResourceAccess(storeResponse.memberId(), CurrentMemberId);

        List<Order> orders = orderRepository.findAllByStoreId(storeId);
        List<SalesResponse.OrderSummary> orderList =
                orders.stream().map(this::toOrderSummary).toList();
        return new SalesResponse(orderList, null, false);
    }

    private SalesResponse.OrderSummary toOrderSummary(Order order) {
        String displayStatus = displayStatus(order);

        List<SalesResponse.ProductSummary> productList =
                order.getOrderDetails().stream()
                        .map(
                                detail ->
                                        new SalesResponse.ProductSummary(
                                                detail.getOrderDetailId(),
                                                detail.getProductName(),
                                                detail.getQuantity(),
                                                detail.getPrice(),
                                                detail.getOptionName()))
                        .toList();

        return new SalesResponse.OrderSummary(
                order.getOrderId(),
                order.getDeliveryAddress().getRecipientName(),
                order.getDeliveryAddress().getRecipientContact(),
                order.getDeliveryAddress().getAddress().toString(),
                order.getCreatedAt(),
                order.getTotalPrice(),
                0,
                order.getTotalPrice() + order.getDeliveryFee(),
                order.getDeliveryFee(),
                productList,
                displayStatus);
    }

    // 환불 존재시 환불 상태 반환, 환불 존재하지 않으면 주문 상태 반환
    private String displayStatus(Order order) {
        Optional<Refund> latestRefundOpt =
                refundRepository.findFirstByOrderOrderByCreatedAtDesc(order);

        if (latestRefundOpt.isPresent()) {
            Refund latestRefund = latestRefundOpt.get();
            RefundStatus refundStatus = latestRefund.getRefundStatus();

            if (refundStatus != RefundStatus.REJECTED) {
                return refundStatus.name();
            }
        }
        return order.getOrderStatusAll().name();
    }

    @Transactional(readOnly = true)
    public BalanceResponse getBalance(UUID storeId) {
        // 1. storeId로 주문 조회
        List<Order> orders = orderRepository.findAllByStoreId(storeId);

        // 2. 총 결제 금액 계산
        int totalPaymentAmount =
                orders.stream().map(Order::getTotalPrice).mapToInt(Integer::intValue).sum();

        // 3. 환불된 금액 계산
        List<Refund> refunds = refundRepository.findByOrderInAndRefundStatus(orders, RefundStatus.COMPLETED);

        int totalRefunds = refunds.stream().mapToInt(Refund::getPrice).sum();

        // 4. 정산 금액 계산
        int settlementAmount = totalPaymentAmount - totalRefunds;

        return new BalanceResponse(totalPaymentAmount, totalRefunds, settlementAmount);
    }
}
