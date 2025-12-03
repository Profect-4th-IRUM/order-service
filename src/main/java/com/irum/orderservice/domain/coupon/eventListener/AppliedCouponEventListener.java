package com.irum.orderservice.domain.coupon.eventListener;

import com.irum.global.advice.exception.CommonException;
import com.irum.orderservice.domain.coupon.event.CouponRollbackEvent;
import com.irum.orderservice.domain.coupon.service.AppliedCouponService;
import com.irum.orderservice.domain.coupon.service.CouponService;
import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.repository.OrderRepository;
import com.irum.orderservice.domain.coupon.event.CouponAppliedEvent;
import com.irum.orderservice.domain.coupon.event.CouponValidatedEvent;
import com.irum.orderservice.global.exception.errorcode.OrderErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppliedCouponEventListener {
    private final AppliedCouponService appliedCouponService;
    private final CouponService couponService;
    private final OrderRepository orderRepository;

    /** 쿠폰 할인 검증 및 계산 (결제 생성 전) */
    @Transactional
    @EventListener
    public void handleCouponValidatedEvent(CouponValidatedEvent event) {
        log.info("쿠폰 할인 계산 시작 - orderId: {}", event.orderId());
        try {
            Order order =
                    orderRepository
                            .findById(event.orderId())
                            .orElseThrow(() -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND));

            // 쿠폰 할인 계산
            int couponDiscount = 0;
            if (event.couponIdList() != null && !event.couponIdList().isEmpty()) {
                couponDiscount =
                        couponService.validAndCalCoupon(
                                event.couponIdList(), order.getTotalPrice(), order.getMemberId());
            }

            int totalDiscount = couponDiscount + order.getTotalDiscountAmount();
            int finalPaymentAmount = order.getTotalPrice() - totalDiscount;

            log.info("할인 금액 {} , 결제 금액 {} ", totalDiscount, finalPaymentAmount);

            order.updateAmount(totalDiscount, finalPaymentAmount);

        } catch (Exception e) {
            log.error("쿠폰 할인 계산 실패 - orderId: {}", event.orderId(), e);
            //            publishOrderFailed(event.getOrderId(), null, null,
            //                    OrderFailedEvent.FailureStep.COUPON_APPLICATION, "쿠폰 할인 계산 실패",
            // e);
        }
    }

    /** 쿠폰 차감 이벤트, 비동기 */
    @Async
    @Transactional
    @EventListener
    public void handleCouponAppliedEvent(CouponAppliedEvent event) {
        log.info("쿠폰 차감 시작 - paymentId: {}", event.paymentId());
        try {
            if (event.couponIdList() != null && !event.couponIdList().isEmpty()) {
                appliedCouponService.createAppliedCouponList(
                        event.paymentId(), event.couponIdList());
                log.info("쿠폰 차감 완료 - couponCount: {}", event.couponIdList().size());
            }
        } catch (Exception e) {
            log.error("쿠폰 차감 실패 - paymentId: {}", event.paymentId(), e);
            //            Order order = orderRepository.findById(event.getOrderId()).orElse(null);
            //            List<UUID> couponIds = order != null ? order.getCouponIds() : null;
            //            publishOrderFailed(event.getOrderId(), event.getPaymentId(), couponIds,
            //                    OrderFailedEvent.FailureStep.COUPON_APPLICATION, "쿠폰 차감 실패", e);
        }
    }

    /** 쿠폰 롤백 이벤트 */
    @Async
    @TransactionalEventListener(phase= TransactionPhase.AFTER_COMMIT)
    public void handleCouponRollbackEvent(CouponRollbackEvent event) {
        log.info("쿠폰 롤백 시작 - paymentId: {}", event.paymentId());
        try{
            appliedCouponService.rollbackAppliedCouponList(event.paymentId());
        }catch (Exception e){
            log.error("쿠폰 롤백 실패 - paymentId: {}", event.paymentId());
            log.error("[에러] 원인 : {}, {}", e.getClass(), e.getMessage());
        }
    }
}
