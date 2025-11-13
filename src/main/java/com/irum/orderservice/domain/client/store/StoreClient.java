package com.irum.orderservice.domain.client.store;

import com.irum.orderservice.domain.client.store.api.StoreAPI;
import com.irum.orderservice.domain.client.store.dto.response.StoreResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreClient {
    private final StoreAPI storeAPI;

    public StoreResponse getStoreId(UUID storeId) {
        return storeAPI.getStoreId(storeId);
    }
}
