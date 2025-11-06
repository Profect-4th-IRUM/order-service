package com.irum.orderservice.domain.client.product.dto.request;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record RollbackStockRequest(
        List<OptionValueRequest> optionValueList
) {
    @Builder
    public record OptionValueRequest(
            UUID optionValueId,
            int quantity
    ){}
}
