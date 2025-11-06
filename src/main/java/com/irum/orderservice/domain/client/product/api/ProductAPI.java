package com.irum.orderservice.domain.client.product.api;

import com.irum.orderservice.domain.client.product.dto.request.ProductInternalRequest;
import com.irum.orderservice.domain.client.product.dto.response.ProductInternalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "product-api",
        url = "product-service/internal/products/",
        configuration = FeignClient.class)
public interface ProductAPI {
    @GetMapping("stock")
    ProductInternalResponse updateStock(@RequestBody ProductInternalRequest request);
}
