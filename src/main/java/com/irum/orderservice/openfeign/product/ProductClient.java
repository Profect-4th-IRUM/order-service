package com.irum.orderservice.openfeign.product;

import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.dto.request.CustomerOrderRequest;
import com.irum.orderservice.openfeign.product.api.ProductAPI;
import com.irum.orderservice.openfeign.product.dto.request.ProductInternalRequest;
import com.irum.orderservice.openfeign.product.dto.request.RollbackStockRequest;
import com.irum.orderservice.openfeign.product.dto.response.ProductInternalResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductClient {
    private final ProductAPI productAPI;

    public void rollbackStock(List<OrderDetail> orderDetailList) {

        List<RollbackStockRequest.OptionValueRequest> optionValueRequestList =
                orderDetailList.stream()
                        .map(
                                o ->
                                        RollbackStockRequest.OptionValueRequest.builder()
                                                .optionValueId(o.getOptionValueId())
                                                .quantity(o.getQuantity())
                                                .build())
                        .toList();

        RollbackStockRequest rollbackStockRequest =
                RollbackStockRequest.builder().optionValueList(optionValueRequestList).build();

        productAPI.rollbackStock(rollbackStockRequest);
    }

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
