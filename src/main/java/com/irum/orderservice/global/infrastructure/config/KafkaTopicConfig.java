package com.irum.orderservice.global.infrastructure.config;


import com.irum.orderservice.global.infrastructure.properties.KafkaProperties;
import com.irum.orderservice.global.infrastructure.properties.KafkaTopicProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfig {
    private final KafkaTopicProperties kafkaTopicProperties;
    private final KafkaProperties kafkaProperties;

    @Bean
    public NewTopic orderFailedTopic() {
        return TopicBuilder.name(kafkaTopicProperties.orderFailed())
                .partitions(kafkaProperties.partition())
                .replicas(kafkaProperties.replica())
                .config(
                        TopicConfig.RETENTION_MS_CONFIG,
                        String.valueOf(7 * 24 * 60 * 60 * 1000L) // 7일간 메시지 보관
                        )
                .build();
    }

}
