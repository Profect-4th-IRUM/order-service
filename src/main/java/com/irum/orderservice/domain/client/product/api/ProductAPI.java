package com.irum.orderservice.domain.client.product.api;

import com.irum.orderservice.domain.client.product.dto.request.ProductInternalRequest;
import com.irum.orderservice.domain.client.product.dto.response.ProductInternalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-api", url = "product-service", configuration = FeignClient.class)
public interface ProductAPI {
    @PatchMapping
    ProductInternalResponse getProductList(@RequestBody ProductInternalRequest request);
}
