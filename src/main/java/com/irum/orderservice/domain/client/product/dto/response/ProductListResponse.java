package com.irum.orderservice.domain.client.product.dto.response;

import java.util.List;
import java.util.UUID;

public record ProductListResponse(
        int defaultDeliveryFee,
        int minAmount,
        int minQuantity,
        UUID storeId,
        List<ProductResponse> productList
) {
    public record ProductResponse(
            UUID productId,
            UUID optionValueId,
            int price,
            int extraPrice,
            int productDiscount,
            String optionName,
            String productName
    ){

    }
}
