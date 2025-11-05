package com.irum.orderservice.domain.client.product;

import com.irum.orderservice.domain.client.product.api.ProductAPI;
import com.irum.orderservice.domain.client.product.dto.request.ProductInternalRequest;
import com.irum.orderservice.domain.client.product.dto.response.ProductInternalResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClient {
    private final ProductAPI productAPI;

    public ProductInternalResponse getProductList(List<UUID> optionValueIds, UUID storeId) {
        ProductInternalRequest request =
                ProductInternalRequest.builder()
                        .storeId(storeId)
                        .optionValueIdList(optionValueIds)
                        .build();
        return productAPI.getProductList(request);
    }
}
