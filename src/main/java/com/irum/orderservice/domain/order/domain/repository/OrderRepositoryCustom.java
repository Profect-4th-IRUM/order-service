package com.irum.orderservice.domain.order.domain.repository;

import com.irum.orderservice.domain.order.domain.entity.enums.OrderStatus;
import com.irum.orderservice.domain.order.repository.dto.CustomerOrderDetailRow;
import com.irum.orderservice.domain.order.repository.dto.CustomerOrderSummaryRow;
import com.irum.orderservice.domain.order.repository.dto.OrderDetailRow;
import com.irum.orderservice.domain.order.repository.dto.OrderSummaryRow;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OrderRepositoryCustom {
    List<OrderSummaryRow> fetchOrderHeaderList(
            UUID storeId, OrderStatus orderStatus, UUID cursor, int size);

    List<OrderDetailRow> fetchOrderDetailList(List<UUID> orderIdList);

    List<CustomerOrderSummaryRow> fetchOrderListByMember(
            Long memberId, LocalDate startDate, LocalDate endDate, UUID cursor, int size);

    List<CustomerOrderDetailRow> fetchOrderDetailListByMember(List<UUID> orderIdList);
}
