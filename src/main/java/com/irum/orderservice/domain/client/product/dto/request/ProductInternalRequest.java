package com.irum.orderservice.domain.client.product.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ProductInternalRequest(List<OptionValueRequest> optionValueList, UUID storeId) {
    @Builder
    public record OptionValueRequest(UUID optionValueId, int quantity) {}
}
