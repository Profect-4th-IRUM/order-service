package com.irum.orderservice.domain.client.store.api;

import com.irum.orderservice.domain.client.store.dto.response.StoreResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "STORE-SERVICE",
        url = "store-service/internal/service",
        configuration = FeignClient.class)
public interface StoreAPI {

    @GetMapping("/stores/{storeId}")
    StoreResponse getStoreId(@PathVariable UUID storeId);
}
