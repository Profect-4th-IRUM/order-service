package com.irum.orderservice.openfeign.store.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record StoreResponse(UUID storeId, Long memberId) {}
