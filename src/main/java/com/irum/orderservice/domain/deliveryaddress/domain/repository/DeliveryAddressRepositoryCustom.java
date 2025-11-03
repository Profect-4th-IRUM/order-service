package com.irum.orderservice.domain.deliveryaddress.repository;

import com.irum.orderservice.domain.deliveryaddress.dto.response.DeliveryAddressInfoResponse;
import java.util.List;
import java.util.UUID;

public interface DeliveryAddressRepositoryCustom {
    List<DeliveryAddressInfoResponse> findDeliveryAddressByCursor(
            Long memberId, UUID cursor, int pageSize);
}
