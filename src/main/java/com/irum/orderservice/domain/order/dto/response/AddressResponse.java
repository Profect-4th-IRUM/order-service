package com.irum.orderservice.domain.order.dto.response;


import com.irum.orderservice.domain.deliveryaddress.domain.entity.Address;

public record AddressResponse(
        String postalCode, String city, String sigungu, String roadname, String addressDetail) {
    public static AddressResponse from(Address address) {
        return new AddressResponse(
                address.getPostalCode(),
                address.getCity(),
                address.getSigungu(),
                address.getRoadName(),
                address.getAddressDetail());
    }
}
