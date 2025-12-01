package com.irum.orderservice.domain.order.producer;

import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.event.OrderFailedEvent;
import com.irum.orderservice.global.infrastructure.properties.KafkaTopicProperties;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderFailedEvent> orderFailedEventKafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    public void sendOrderFailedEvent(List<OrderDetail> orderDetailList, UUID orderId) {
        // create event
        List<OrderFailedEvent.OptionValueRequest> optionValueRequestList =
                orderDetailList.stream().map(OrderFailedEvent.OptionValueRequest::from).toList();
        OrderFailedEvent event = OrderFailedEvent.from(optionValueRequestList);

        // create key & record
        String key = orderId.toString();
        ProducerRecord<String, OrderFailedEvent> record =
                new ProducerRecord<>(kafkaTopicProperties.orderFailed(), key, event);

        try {
            orderFailedEventKafkaTemplate
                    .send(record)
                    .whenComplete(
                            (result, exception) -> {
                                if (exception == null) {
                                    // 성공
                                    log.debug("[Success] Sent order failed event: {}", event);
                                } else {
                                    // 실패 처리
                                    log.error(
                                            "[Error] sending order failed event : {}",
                                            exception.getMessage(),
                                            exception);
                                }
                            });
        } catch (Exception e) {
            log.error("[Error] sending order failed event : {}", event, e);
        }
    }
}
