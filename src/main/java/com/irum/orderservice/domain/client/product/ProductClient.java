package com.irum.orderservice.domain.client.product;

import com.irum.orderservice.domain.client.product.api.ProductAPI;
import com.irum.orderservice.domain.client.product.dto.response.ProductListResponse;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductClient {
    private final ProductAPI productAPI;

    public ProductListResponse getProductList(@RequestParam List<UUID> optionValueIds, @RequestParam UUID storeId) {
        return productAPI.getProductList(optionValueIds, storeId);
    }
}
