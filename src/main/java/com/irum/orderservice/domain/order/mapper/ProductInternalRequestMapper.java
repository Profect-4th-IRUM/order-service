package com.irum.orderservice.domain.order.mapper;

import com.irum.openfeign.product.dto.request.ProductInternalRequest;
import com.irum.orderservice.domain.order.dto.request.CustomerOrderRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductInternalRequestMapper {

    public static ProductInternalRequest toProductInternalRequest(CustomerOrderRequest request) {
        List<ProductInternalRequest.OptionValueRequest> optionValueRequestList =
                request.productList().stream()
                        .map(
                                p ->
                                        ProductInternalRequest.OptionValueRequest.builder()
                                                .optionValueId(p.optionValueId())
                                                .quantity(p.quantity())
                                                .build())
                        .toList();
        return ProductInternalRequest.builder()
                        .storeId(request.storeId())
                        .optionValueList(optionValueRequestList)
                        .build();
    }
}
