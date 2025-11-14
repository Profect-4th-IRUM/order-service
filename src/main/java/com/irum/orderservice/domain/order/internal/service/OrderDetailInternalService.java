package com.irum.orderservice.domain.order.internal.service;

import com.irum.global.advice.exception.CommonException;
import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.domain.repository.OrderDetailRepository;
import com.irum.orderservice.domain.order.internal.dto.response.OrderDetailInternalResponse;
import com.irum.orderservice.domain.order.mapper.OrderDetailInternalMapper;
import com.irum.orderservice.global.exception.errorcode.OrderErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderDetailInternalService {

    private final OrderDetailRepository orderDetailRepository;

    @Transactional(readOnly = true)
    public OrderDetailInternalResponse getOrderDetail(UUID orderDetailId) {
        OrderDetail orderDetail =
                orderDetailRepository
                        .findByIdWithOrder(orderDetailId)
                        .orElseThrow(
                                () -> new CommonException(OrderErrorCode.ORDER_DETAIL_NOT_FOUND));
        return OrderDetailInternalMapper.toResponse(
                orderDetail, orderDetail.getOrder().getMemberId());
    }
}
