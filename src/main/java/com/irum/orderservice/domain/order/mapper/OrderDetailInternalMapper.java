package com.irum.orderservice.domain.order.mapper;

import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import com.irum.orderservice.domain.order.internal.dto.response.OrderDetailInternalResponse;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailInternalMapper {

    /** 단일 주문 상세 변환 */
    public static OrderDetailInternalResponse toResponse(OrderDetail orderDetail, Long memberId) {
        return new OrderDetailInternalResponse(
                orderDetail.getOrderDetailId(),
                orderDetail.getProductId(),
                memberId,
                orderDetail.getOrderStatusIndi().name());
    }

    /** 주문 상세 리스트 변환 */
    public static List<OrderDetailInternalResponse> toResponseList(
            List<OrderDetail> orderDetailList, Long memberId) {
        return orderDetailList.stream()
                .map(orderDetail -> toResponse(orderDetail, memberId))
                .toList();
    }
}
