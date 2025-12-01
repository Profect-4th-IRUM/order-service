package com.irum.orderservice.domain.order.consumer;

import com.irum.orderservice.domain.order.event.PaymentFailedEvent;
import com.irum.orderservice.domain.order.event.PaymentPaidEvent;
import com.irum.orderservice.domain.order.internal.service.OrderInternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {
    private final OrderInternalService orderInternalService;

    @KafkaListener(
            topics = "${spring.kafka.topics.payment-paid}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void handlePaymentPaid(PaymentPaidEvent event) {
        log.info("[외부] Payment Paid event 수신 완료 {}", event);

        orderInternalService.updateOrderStatusPreparing(event);
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.payment-failed}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("[외부] Payment Failed event 수신 완료 {}", event);

        orderInternalService.updateOrderStatusFailed(event);
    }
}
