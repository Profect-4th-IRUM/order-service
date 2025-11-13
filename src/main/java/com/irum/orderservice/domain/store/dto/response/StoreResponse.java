package com.irum.orderservice.domain.store.dto.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record StoreResponse(UUID storeId, Long memberId) {}
