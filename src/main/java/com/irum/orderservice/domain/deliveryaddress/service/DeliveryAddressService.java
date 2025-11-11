package com.irum.orderservice.domain.deliveryaddress.service;

import com.irum.global.advice.exception.CommonException;
import com.irum.global.advice.exception.errorcode.GlobalErrorCode;
import com.irum.orderservice.domain.deliveryaddress.domain.entity.DeliveryAddress;
import com.irum.orderservice.domain.deliveryaddress.domain.repository.DeliveryAddressRepository;
import com.irum.orderservice.domain.deliveryaddress.dto.request.AddressDetailUpdateRequest;
import com.irum.orderservice.domain.deliveryaddress.dto.request.DeliveryAddressRegisterRequest;
import com.irum.orderservice.domain.deliveryaddress.dto.request.RecipientUpdateRequest;
import com.irum.orderservice.domain.deliveryaddress.dto.response.DeliveryAddressInfoListResponse;
import com.irum.orderservice.domain.deliveryaddress.dto.response.DeliveryAddressInfoResponse;
import com.irum.orderservice.global.exception.errorcode.DeliveryAddressErrorCode;
import com.irum.orderservice.global.util.MemberUtil;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openfeign.member.dto.response.MemberDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeliveryAddressService {
    private final MemberUtil memberUtil;
    private final DeliveryAddressRepository deliveryAddressRepository;

    public void createDeliveryAddress(DeliveryAddressRegisterRequest request) {
        MemberDto member = memberUtil.getCurrentMember();
        String recipientName =
                request.recipientName() == null ? member.name() : request.recipientName();
        String recipientContact =
                request.recipientContact() == null ? member.contact() : request.recipientContact();
        DeliveryAddress deliveryAddress =
                DeliveryAddress.create(
                        member.memberId(), request.address(), recipientName, recipientContact);
        if (!deliveryAddressRepository.existsByMember(member.memberId()))
            deliveryAddress.markAsDefault();
        deliveryAddressRepository.save(deliveryAddress);
    }

    @Transactional(readOnly = true)
    public DeliveryAddressInfoResponse findDeliveryAddress(UUID deliveryAddressId) {
        DeliveryAddress deliveryAddress = validDeliveryAddress(deliveryAddressId);
        return DeliveryAddressInfoResponse.of(
                deliveryAddress.getDeliveryAddressId(),
                deliveryAddress.getAddress(),
                deliveryAddress.getRecipientName(),
                deliveryAddress.getRecipientContact(),
                deliveryAddress.isDefault());
    }

    @Transactional(readOnly = true)
    public DeliveryAddressInfoListResponse findDeliveryAddressList(UUID cursor, Integer size) {
        if (size == null || (size != 10 && size != 30 && size != 50)) {
            log.warn("허용되지 않은 size 요청: {} -> 기본값 10으로 대체", size);
            size = 10;
        }
        int limit = size + 1;
        List<DeliveryAddressInfoResponse> addressList =
                deliveryAddressRepository.findDeliveryAddressByCursor(
                        memberUtil.getCurrentMember().memberId(), cursor, limit);
        boolean hasNext = addressList.size() > size;
        List<DeliveryAddressInfoResponse> resultList =
                hasNext ? addressList.subList(0, size) : addressList;
        UUID nextCursor = null;
        if (hasNext) {
            DeliveryAddressInfoResponse lastItem = resultList.get(resultList.size() - 1);
            nextCursor = lastItem.deliveryAddressId();
        }
        return new DeliveryAddressInfoListResponse(resultList, nextCursor, hasNext);
    }

    public void changeRecipientInfo(UUID deliveryAddressId, RecipientUpdateRequest request) {
        DeliveryAddress deliveryAddress = validDeliveryAddress(deliveryAddressId);
        applyValidUpdate(
                deliveryAddress, request.newRecipientName(), request.newRecipientContact());
    }

    public void changeAddressDetail(UUID deliveryAddressId, AddressDetailUpdateRequest request) {
        DeliveryAddress deliveryAddress = validDeliveryAddress(deliveryAddressId);
        deliveryAddress.updateAddressDetail(request.newAddressDetail());
    }

    public void changeDefaultDeliveryAddress(UUID deliveryAddressId) {
        getCurrentDefaultAddress().unmarkAsDefault();
        DeliveryAddress deliveryAddress = validDeliveryAddress(deliveryAddressId);
        deliveryAddress.markAsDefault();
    }

    public void removeDeliveryAddress(UUID deliveryAddressId) {
        MemberDto member = memberUtil.getCurrentMember();
        DeliveryAddress address = validDeliveryAddress(deliveryAddressId);
        if (address.isDefault()) {
            // DeliveryAddress 중 가장 최근 것 기본 배송지 설정
            deliveryAddressRepository
                    .findTopByMemberOrderByCreatedAtDesc(member.memberId())
                    .ifPresent(DeliveryAddress::markAsDefault);
        }
        memberUtil.assertMemberResourceAccess(address.getMemberId(), member.memberId());
        address.softDelete(member.memberId());
    }

    private DeliveryAddress validDeliveryAddress(UUID deliveryAddressId) {
        DeliveryAddress address =
                deliveryAddressRepository
                        .findById(deliveryAddressId)
                        .orElseThrow(
                                () ->
                                        new CommonException(
                                                DeliveryAddressErrorCode
                                                        .DELIVERY_ADDRESS_NOT_FOUND));
        memberUtil.assertMemberResourceAccess(address.getMemberId());
        return address;
    }

    private DeliveryAddress getCurrentDefaultAddress() {
        MemberDto member = memberUtil.getCurrentMember();
        return deliveryAddressRepository
                .findDefaultAddressByMember(member.memberId())
                .orElseThrow(
                        () ->
                                new CommonException(
                                        DeliveryAddressErrorCode.DELIVERY_ADDRESS_NOT_FOUND));
    }

    public void applyValidUpdate(DeliveryAddress address, String newName, String newContact) {
        if (!StringUtils.hasText(newName) && !StringUtils.hasText(newContact))
            throw new CommonException(GlobalErrorCode.EMPTY_REQUEST);
        if (StringUtils.hasText(newName)) address.updateRecipientName(newName);
        if (StringUtils.hasText(newContact)) address.updateRecipientContact(newContact);
    }
}
