package com.irum.orderservice.domain.order.service;

import com.irum.global.advice.exception.CommonException;
import com.irum.openfeign.payment.client.PaymentClient;
import com.irum.openfeign.payment.dto.request.CreatePaymentRequest;
import com.irum.openfeign.payment.dto.response.PaymentResponse;
import com.irum.openfeign.payment.emuns.PaymentCorp;
import com.irum.openfeign.product.client.ProductClient;
import com.irum.openfeign.product.dto.request.ProductInternalRequest;
import com.irum.openfeign.product.dto.response.ProductInternalResponse;
import com.irum.orderservice.domain.coupon.service.AppliedCouponService;
import com.irum.orderservice.domain.coupon.service.CouponService;
import com.irum.orderservice.domain.deliveryaddress.domain.entity.DeliveryAddress;
import com.irum.orderservice.domain.deliveryaddress.domain.repository.DeliveryAddressRepository;
import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.domain.repository.OrderDetailRepository;
import com.irum.orderservice.domain.order.domain.repository.OrderRepository;
import com.irum.orderservice.domain.order.dto.request.CustomerOrderRequest;
import com.irum.orderservice.domain.order.dto.response.CustomerOrderListResponse;
import com.irum.orderservice.domain.order.dto.response.CustomerOrderResponse;
import com.irum.orderservice.domain.order.dto.response.OrderDetailResponse;
import com.irum.orderservice.domain.order.dto.response.OrderDetailStatusResponse;
import com.irum.orderservice.domain.order.mapper.CustomerOrderMapper;
import com.irum.orderservice.domain.order.repository.dto.CustomerOrderDetailRow;
import com.irum.orderservice.domain.order.repository.dto.CustomerOrderSummaryRow;
import com.irum.orderservice.domain.refund.domain.entity.Refund;
import com.irum.orderservice.domain.refund.domain.repository.RefundRepository;
import com.irum.orderservice.global.exception.errorcode.DeliveryAddressErrorCode;
import com.irum.orderservice.global.exception.errorcode.OrderErrorCode;
import com.irum.orderservice.global.util.MemberUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustomerOrderService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final RefundRepository refundRepository;
    private final MemberUtil memberUtil;
    private final DeliveryAddressRepository deliveryAddressRepository;
    private final CouponService couponService;
    private final AppliedCouponService appliedCouponService;

    private final PaymentClient paymentClient;
    private final ProductClient productClient;

    @Transactional(readOnly = true)
    public OrderDetailStatusResponse getOrderDetailStatus(UUID orderDetailId) {
        Long currentMemberId = memberUtil.getCurrentMember().memberId();

        OrderDetail orderDetail =
                orderDetailRepository
                        .findByOrderDetailIdWithOrder(orderDetailId)
                        .orElseThrow(
                                () -> new CommonException(OrderErrorCode.ORDER_DETAIL_NOT_FOUND));

        if (currentMemberId.equals(orderDetail.getOrder().getMemberId()))
            throw new CommonException(OrderErrorCode.ORDER_FORBIDDEN);

        return OrderDetailStatusResponse.from(orderDetail);
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(UUID orderId) {
        Long currentMemberId = memberUtil.getCurrentMember().memberId();

        // order 조회 및 member 검증
        Order order =
                orderRepository
                        .findByOrderIdAndMemberId(orderId, currentMemberId)
                        .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));

        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrder(order);

        Refund refund = refundRepository.findByOrder(order).orElse(null);
        PaymentResponse paymentResponse = paymentClient.getPayment(order.getPaymentId());

        return CustomerOrderMapper.toOrderDetailResponse(
                order, orderDetailList, refund, paymentResponse);
    }

    @Transactional
    public CustomerOrderListResponse getOrderList(
            UUID cursor, int size, LocalDate startDate, LocalDate endDate) {

        Long currentMemberId = memberUtil.getCurrentMember().memberId();

        // 2. order list 검색
        List<CustomerOrderSummaryRow> headerList =
                orderRepository.fetchOrderListByMember(
                        currentMemberId, startDate, endDate, cursor, size);
        log.info("order list {}", headerList);

        boolean hasNext = headerList.size() > size;
        if (hasNext) {
            headerList = headerList.subList(0, size);
        }
        log.info("hasNext {}", hasNext);

        // 3. order id list
        List<UUID> orderIdList = headerList.stream().map(CustomerOrderSummaryRow::orderId).toList();
        List<CustomerOrderDetailRow> orderDetailList =
                orderRepository.fetchOrderDetailListByMember(orderIdList);
        log.info("orderDetailList {}", orderDetailList);

        // 4. orderId로 그룹핑 : productSummary 제작
        Map<UUID, List<CustomerOrderListResponse.ProductResponse>> detailMap =
                orderDetailList.stream()
                        .collect(
                                Collectors.groupingBy(
                                        CustomerOrderDetailRow::orderId,
                                        Collectors.mapping(
                                                CustomerOrderMapper::toProductResponse,
                                                Collectors.toList())));

        // 5. orderResponse 제작
        List<CustomerOrderListResponse.OrderResponse> orderResponseList =
                headerList.stream()
                        .filter(order -> detailMap.containsKey(order.orderId()))
                        .map(
                                order ->
                                        CustomerOrderMapper.toOrderResponse(
                                                order,
                                                detailMap.getOrDefault(
                                                        order.orderId(),
                                                        List.of()) // order detail 없다면 빈 리스트
                                                ))
                        .toList();

        // 6. next cursor계산
        UUID nextCursor = orderResponseList.isEmpty() ? null : headerList.getLast().orderId();

        return CustomerOrderListResponse.builder()
                .orderList(orderResponseList)
                .hasNext(hasNext)
                .nextCursor(nextCursor)
                .build();
    }

    public CustomerOrderResponse prepareOrder(CustomerOrderRequest request) {
        Long currentMemberId = memberUtil.getCurrentMember().memberId();
        int discountAmount = 0;

        DeliveryAddress deliveryAddress =
                deliveryAddressRepository
                        .findById(request.deliveryAddressId())
                        .orElseThrow(
                                () ->
                                        new CommonException(
                                                DeliveryAddressErrorCode
                                                        .DELIVERY_ADDRESS_NOT_FOUND));
        log.info("[주문준비] 멤버 {} , 상점, 주소 검색", currentMemberId);

        List<UUID> productIds =
                request.productList().stream()
                        .map(CustomerOrderRequest.ProductSummary::productId)
                        .distinct()
                        .toList();

        List<UUID> optionValueIds =
                request.productList().stream()
                        .map(CustomerOrderRequest.ProductSummary::optionValueId)
                        .distinct()
                        .toList();

        // 조회, 재고 미리 차감
        List<ProductInternalRequest.OptionValueRequest> optionValueRequestList =
                request.productList().stream()
                        .map(
                                p ->
                                        ProductInternalRequest.OptionValueRequest.builder()
                                                .optionValueId(p.optionValueId())
                                                .quantity(p.quantity())
                                                .build())
                        .toList();
        ProductInternalRequest productInternalRequest =
                ProductInternalRequest.builder()
                        .storeId(request.storeId())
                        .optionValueList(optionValueRequestList)
                        .build();
        ProductInternalResponse response = productClient.updateStock(productInternalRequest);

        Map<UUID, ProductInternalResponse.ProductResponse> optionMap =
                response.productList().stream()
                        .collect(
                                Collectors.toMap(
                                        ProductInternalResponse.ProductResponse::optionValueId,
                                        product -> product));

        // 정합 정검
        if (optionMap.size() != productIds.size() || optionMap.size() != optionValueIds.size()) {
            throw new CommonException(OrderErrorCode.INVALID_ORDER);
        }

        // 상품 확인, 재고확인, 상품 정보 조회 , 가격 계산, 주문 상세 엔티티 생성 준비
        int calculatedTotalPrice = 0;
        int productCount = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CustomerOrderRequest.ProductSummary productReq : request.productList()) {
            ProductInternalResponse.ProductResponse product =
                    optionMap.get(productReq.optionValueId());

            // 제품 가격 계산
            int productPrice = (product.price() + product.extraPrice()) * productReq.quantity();
            calculatedTotalPrice += productPrice;
            // 상품 개수 카운트
            productCount += productReq.quantity();
            // 상품 개별 할인
            discountAmount += product.productDiscount();

            OrderDetail orderDetail =
                    OrderDetail.from(product, productPrice, productReq.quantity());
            orderDetails.add(orderDetail);
        }
        log.info("상품 확인, 재고 확인, 재고 차감, 가격 계산 완료");

        /** 배송비 적용* */
        int deliveryFee = response.defaultDeliveryFee();
        int deliveryMinAmount = response.minAmount();
        int deliveryMinQuantity = response.minQuantity();
        if (calculatedTotalPrice > deliveryMinAmount || productCount > deliveryMinQuantity) {
            deliveryFee = 0;
        }
        log.info("배송비 {}", deliveryFee);

        /** 할인 쿠폰 적용 */
        discountAmount +=
                couponService.validAndCalCoupon(
                        request.couponIdList(), calculatedTotalPrice, currentMemberId);
        int finalPaymentAmount = calculatedTotalPrice - discountAmount;
        log.info("할인 {}, 할인 후 가격 {}", discountAmount, finalPaymentAmount);

        /** 결재 생성 PENDING 상태* */
        CreatePaymentRequest paymentRequest =
                CreatePaymentRequest.builder()
                        .finalPaymentAmount(finalPaymentAmount)
                        .discountAmount(discountAmount)
                        .paymentCorp(PaymentCorp.TOSS)
                        .build();
        UUID paymentId = paymentClient.createPaymentPending(paymentRequest);

        // 쿠폰 미리 차감
        appliedCouponService.createAppliedCouponList(paymentId, request.couponIdList());

        // 주문 엔티티 생성 PENDING 상태  8 자리 랜덤값
        String orderNum = "ORD-" + (int) ((Math.random() * 100000000));

        Order order =
                Order.from(
                        orderNum,
                        calculatedTotalPrice,
                        deliveryFee,
                        request.deliveryRequest(),
                        currentMemberId,
                        request.storeId(),
                        paymentId,
                        deliveryAddress,
                        discountAmount,
                        finalPaymentAmount);
        orderRepository.save(order);

        /** 주문 상세 저장* */
        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.updateOrder(order);
            orderDetailRepository.save(orderDetail);
        }

        return CustomerOrderMapper.toCustomerOrderResponse(
                order, orderDetails, discountAmount, finalPaymentAmount);
    }
}
