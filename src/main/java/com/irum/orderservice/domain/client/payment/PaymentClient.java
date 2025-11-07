package com.irum.orderservice.domain.client.payment;

import com.irum.orderservice.domain.client.payment.api.PaymentAPI;
import com.irum.orderservice.domain.client.payment.dto.request.UpdatePaymentStatusRequest;
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
}
