package com.irum.orderservice.openfeign.payment.client;

import com.irum.orderservice.openfeign.payment.dto.request.CreatePaymentRequest;
import com.irum.orderservice.openfeign.payment.dto.request.UpdatePaymentStatusRequest;
import com.irum.orderservice.openfeign.payment.dto.response.PaymentResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "PAYMENT-SERVICE",
        url = "/internal/payments")
public interface PaymentClient {

    @PatchMapping
    int updateStatusToFailed(@RequestBody UpdatePaymentStatusRequest request);

    @GetMapping("/{paymentId}")
    PaymentResponse getPayment(@PathVariable UUID paymentId);

    @PostMapping
    UUID createPaymentPending(@RequestBody CreatePaymentRequest request);
}
