package com.irum.orderservice.global.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties (
        int partition,
        int replica,
        Topics topics
){
    public record Topics (
            String paymentPaid,
            String paymentFailed
    ){
    }
}
