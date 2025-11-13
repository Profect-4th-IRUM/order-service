package com.irum.orderservice.domain.store.client;

import com.irum.orderservice.domain.store.dto.response.StoreResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "STORE-SERVICE")
public interface StoreClient {

    @GetMapping("/internal/stores/{storeId}")
    StoreResponse getStoreId(@PathVariable UUID storeId);
}
