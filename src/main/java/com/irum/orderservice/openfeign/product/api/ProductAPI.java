package com.irum.orderservice.openfeign.product.api;

import com.irum.orderservice.openfeign.config.FeignConfig;
import com.irum.orderservice.openfeign.product.dto.request.ProductInternalRequest;
import com.irum.orderservice.openfeign.product.dto.request.RollbackStockRequest;
import com.irum.orderservice.openfeign.product.dto.response.ProductInternalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "PRODUCT-SERVICE",
        url = "product-service/internal/products",
        configuration = FeignConfig.class)
public interface ProductAPI {

    /** 재고 롤백 */
    @PatchMapping("/rollback")
    void rollbackStock(@RequestBody RollbackStockRequest request);

    @GetMapping("stock")
    ProductInternalResponse updateStock(@RequestBody ProductInternalRequest request);
}
