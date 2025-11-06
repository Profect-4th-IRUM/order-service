package com.irum.orderservice.domain.client.product;

import com.irum.orderservice.domain.client.product.api.ProductAPI;
import com.irum.orderservice.domain.client.product.dto.request.ProductInternalRequest;
import com.irum.orderservice.domain.client.product.dto.response.ProductInternalResponse;
import com.irum.orderservice.domain.order.dto.request.CustomerOrderRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClient {
    private final ProductAPI productAPI;

    public ProductInternalResponse updateStock(
            List<CustomerOrderRequest.ProductSummary> productList, UUID storeId) {
        List<ProductInternalRequest.OptionValueRequest> optionValueRequestList =
                productList.stream()
                        .map(
                                p ->
                                        ProductInternalRequest.OptionValueRequest.builder()
                                                .optionValueId(p.optionValueId())
                                                .quantity(p.quantity())
                                                .build())
                        .toList();

        ProductInternalRequest request =
                ProductInternalRequest.builder()
                        .storeId(storeId)
                        .optionValueList(optionValueRequestList)
                        .build();
        return productAPI.updateStock(request);
    }
}
