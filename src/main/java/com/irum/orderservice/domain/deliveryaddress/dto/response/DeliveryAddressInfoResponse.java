package com.irum.orderservice.domain.deliveryaddress.dto.response;

import com.irum.orderservice.domain.deliveryaddress.domain.entity.Address;
import java.util.UUID;

public record DeliveryAddressInfoResponse(
        UUID deliveryAddressId,
        Address address,
        String recipientName,
        String recipientContact,
        Boolean isDefault) {
    public static DeliveryAddressInfoResponse of(
            UUID deliveryAddressId,
            Address address,
            String recipientName,
            String recipientContact,
            Boolean isDefault) {
        return new DeliveryAddressInfoResponse(
                deliveryAddressId, address, recipientName, recipientContact, isDefault);
    }
}
