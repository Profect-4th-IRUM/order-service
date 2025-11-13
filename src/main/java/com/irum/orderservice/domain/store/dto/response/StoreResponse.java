package com.irum.orderservice.domain.store.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record StoreResponse(UUID storeId, Long memberId) {}
