package com.irum.orderservice.domain.client.payment.api;

import com.irum.orderservice.domain.client.config.FeignConfig;
import com.irum.orderservice.domain.client.payment.dto.request.UpdatePaymentStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import com.irum.orderservice.domain.client.payment.dto.request.CreatePaymentRequest;
import com.irum.orderservice.domain.client.payment.dto.response.PaymentResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-api", url = "payment-service", configuration = FeignConfig.class)
public interface PaymentAPI {

    @PatchMapping
    int updateStatusToFailed(@RequestBody UpdatePaymentStatusRequest request);

    @GetMapping("/{paymentId}")
    PaymentResponse getPayment(@PathVariable UUID paymentId);

    @PostMapping
    UUID createPaymentPending(@RequestBody CreatePaymentRequest request);
}
