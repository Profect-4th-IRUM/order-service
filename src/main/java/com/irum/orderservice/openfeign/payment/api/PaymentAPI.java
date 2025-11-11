package com.irum.orderservice.openfeign.payment.api;

import com.irum.orderservice.openfeign.config.FeignConfig;
import com.irum.orderservice.openfeign.payment.dto.request.CreatePaymentRequest;
import com.irum.orderservice.openfeign.payment.dto.request.UpdatePaymentStatusRequest;
import com.irum.orderservice.openfeign.payment.dto.response.PaymentMapResponse;
import com.irum.orderservice.openfeign.payment.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PAYMENT-SERVICE", url = "payment-service", configuration = FeignConfig.class)
public interface PaymentAPI {

    @PatchMapping
    int updateStatusToFailed(@RequestBody UpdatePaymentStatusRequest request);

    @GetMapping("/{paymentId}")
    PaymentResponse getPayment(@PathVariable UUID paymentId);

    /** payment map 가져오기 */
    @GetMapping("/map")
    PaymentMapResponse getPaymentMap(@RequestParam List<UUID> paymentIdList);

    @PostMapping
    UUID createPaymentPending(@RequestBody CreatePaymentRequest request);
}
