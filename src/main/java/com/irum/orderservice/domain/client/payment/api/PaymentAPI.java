package com.irum.orderservice.domain.client.payment.api;


import com.irum.orderservice.domain.client.config.FeignConfig;
import com.irum.orderservice.domain.client.payment.dto.request.UpdatePaymentStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-api", url = "payment-service", configuration = FeignConfig.class)
public interface PaymentAPI {

    @PatchMapping
    int updateStatusToFailed(@RequestBody UpdatePaymentStatusRequest request);
}