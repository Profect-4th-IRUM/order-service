package com.irum.orderservice.domain.client.product.api;

import com.irum.orderservice.domain.client.config.FeignConfig;
import com.irum.orderservice.domain.client.product.dto.request.ProductInternalRequest;
import com.irum.orderservice.domain.client.product.dto.request.RollbackStockRequest;
import com.irum.orderservice.domain.client.product.dto.response.ProductInternalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "product-service",
        url = "product-service/internal/products",
        configuration = FeignConfig.class)
public interface ProductAPI {

    /** 재고 롤백 */
    @PatchMapping("/rollback")
    void rollbackStock(@RequestBody RollbackStockRequest request);

    @GetMapping("stock")
    ProductInternalResponse updateStock(@RequestBody ProductInternalRequest request);
}
