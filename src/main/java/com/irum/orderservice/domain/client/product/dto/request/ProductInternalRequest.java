package com.irum.orderservice.domain.client.product.dto.request;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ProductInternalRequest (
        List<UUID> optionValueIdList,
        UUID storeId
){
}
