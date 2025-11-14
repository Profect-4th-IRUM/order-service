package com.irum.orderservice.openfeign.store.client;

import com.irum.orderservice.openfeign.store.dto.response.StoreResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface StoreClient {
    @GetMapping("/internal/stores/{storeId}/owner")
    StoreResponse getStoreId(@PathVariable UUID storeId);
}
