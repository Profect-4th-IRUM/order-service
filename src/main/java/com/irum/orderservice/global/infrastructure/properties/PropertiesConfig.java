package com.irum.orderservice.global.infrastructure.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
    RedisProperties.class,
    TossProperties.class,
    FileProperties.class,
    KafkaProperties.class,
    KafkaTopicProperties.class
})
public class PropertiesConfig {}
