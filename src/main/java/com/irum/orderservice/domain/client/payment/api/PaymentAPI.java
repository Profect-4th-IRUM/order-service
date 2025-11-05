package com.irum.orderservice.domain.client.payment.api;

import com.irum.orderservice.domain.client.config.FeignConfig;
import com.irum.orderservice.domain.client.payment.dto.response.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "payment-api",
        url = "payment-service",
        configuration = FeignConfig.class
)
public interface PaymentAPI {

    @GetMapping
    PaymentResponse getPayment(@PathVariable UUID paymentId);

}
