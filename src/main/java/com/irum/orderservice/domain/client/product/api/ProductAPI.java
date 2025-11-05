package com.irum.orderservice.domain.client.product.api;

import com.irum.orderservice.domain.client.product.dto.response.ProductListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(
        name = "product-api",
        url = "product-service",
        configuration = FeignClient.class
)
public interface ProductAPI {
    @GetMapping
    ProductListResponse getProductList(@RequestParam List<UUID> optionValueIds, @RequestParam UUID storeId);
}
