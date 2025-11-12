package com.irum.orderservice.domain.client.store.dto.response;

import java.util.UUID;

public record StoreResponse(
        UUID storeId,
        Long memberId
) {
}
