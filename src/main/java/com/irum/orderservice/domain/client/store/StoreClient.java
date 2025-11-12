package com.irum.orderservice.domain.client.store;

import com.irum.orderservice.domain.client.payment.api.PaymentAPI;
import com.irum.orderservice.domain.client.payment.dto.response.PaymentResponse;
import com.irum.orderservice.domain.client.store.api.StoreAPI;
import com.irum.orderservice.domain.client.store.dto.response.StoreResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StoreClient {
    private final StoreAPI storeAPI;

    public StoreResponse getStoreId(UUID storeId) {
        return storeAPI.getStoreId(storeId);
    }

}
