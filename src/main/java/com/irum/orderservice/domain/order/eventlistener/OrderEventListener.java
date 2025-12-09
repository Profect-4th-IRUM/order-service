package com.irum.orderservice.domain.order.eventlistener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irum.global.advice.exception.CommonException;
import com.irum.orderservice.domain.order.domain.entity.OrderOutbox;
import com.irum.orderservice.domain.order.domain.repository.OrderOutboxRepository;
import com.irum.orderservice.domain.order.event.OrderFailedOutboxEvent;
import com.irum.orderservice.global.exception.errorcode.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {
    private final ObjectMapper objectMapper;
    private final OrderOutboxRepository orderOutboxRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOrderFailedOutboxEvent(OrderFailedOutboxEvent event) {
        saveOutbox(event.payload(), event.orderId().toString());
        log.info("[DB] OrderFailed Outbox 저장 완료. orderId={}", event.orderId());
    }

    private void saveOutbox(Object payloadObj, String orderId){
        try {
            String jsonPayload = objectMapper.writeValueAsString(payloadObj);
            OrderOutbox outbox = OrderOutbox.builder()
                    .aggregateId(orderId)
                    .aggregateType("p_order")
                    .type("failed")
                    .payload(jsonPayload)
                    .build();
            orderOutboxRepository.save(outbox);
        } catch (JsonProcessingException e){
            log.error("[비즈니스] outbox 저장 중 메시지 Json 변환 에러");
            throw new CommonException(GlobalErrorCode.JSON_PROCESSING_EXCEPTION);
        }
    }
}
