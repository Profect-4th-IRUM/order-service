package com.irum.orderservice.openfeign.payment;

import com.irum.orderservice.openfeign.payment.api.PaymentAPI;
import com.irum.orderservice.openfeign.payment.dto.emuns.PaymentCorp;
import com.irum.orderservice.openfeign.payment.dto.request.CreatePaymentRequest;
import com.irum.orderservice.openfeign.payment.dto.request.UpdatePaymentStatusRequest;
import com.irum.orderservice.openfeign.payment.dto.response.PaymentResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentClient {
    private final PaymentAPI paymentAPI;

    public int updateStatusToFailed(List<UUID> paymentIdList) {
        UpdatePaymentStatusRequest request =
                UpdatePaymentStatusRequest.builder().paymentIdList(paymentIdList).build();
        return paymentAPI.updateStatusToFailed(request);
    }

    public PaymentResponse getPayment(UUID paymentId) {
        return paymentAPI.getPayment(paymentId);
    }

    public UUID createPaymentPending(
            int finalPaymentAmount, int discountAmount, PaymentCorp paymentCorp) {
        CreatePaymentRequest request =
                CreatePaymentRequest.builder()
                        .finalPaymentAmount(finalPaymentAmount)
                        .discountAmount(discountAmount)
                        .paymentCorp(paymentCorp)
                        .build();
        return paymentAPI.createPaymentPending(request);
    }
}
