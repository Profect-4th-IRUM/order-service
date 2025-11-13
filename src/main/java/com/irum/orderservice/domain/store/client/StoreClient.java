package com.irum.orderservice.domain.store.client;

import com.irum.orderservice.domain.store.dto.response.StoreResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

public interface StoreClient {
    @GetMapping("/internal/stores/{storeId}/owner")
    StoreResponse getStoreId(@PathVariable UUID storeId);
}
