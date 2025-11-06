package com.irum.orderservice.domain.order.mapper;

import com.irum.orderservice.domain.order.domain.entity.Order;
import com.irum.orderservice.domain.order.domain.entity.enums.OrderStatus;
import com.irum.orderservice.domain.order.domain.repository.OrderDetailRepository;
import com.irum.orderservice.domain.order.domain.repository.OrderRepository;
import com.irum.orderservice.global.presentation.advice.exception.CommonException;
import com.irum.orderservice.global.presentation.advice.exception.errorcode.OrderErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderInternalService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public void updateOrderStatusPreparing(UUID orderId){
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(
                () -> new CommonException(OrderErrorCode.ORDER_NOT_FOUND)
        );
        order.updateOrderStatus(OrderStatus.PREPARING);

        orderDetailRepository.updateStatusToPreparingByOrderId(orderId);
    }
}
