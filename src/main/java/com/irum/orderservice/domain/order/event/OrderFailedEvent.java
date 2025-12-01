package com.irum.orderservice.domain.order.event;

import com.irum.orderservice.domain.order.domain.entity.OrderDetail;
import java.util.List;
import java.util.UUID;

public record OrderFailedEvent(List<OptionValueRequest> optionValueList) {
    public record OptionValueRequest(UUID optionValueId, int quantity) {
        public static OptionValueRequest from(OrderDetail orderDetail) {
            return new OptionValueRequest(
                    orderDetail.getOptionValueId(), orderDetail.getQuantity());
        }
    }

    public static OrderFailedEvent from(List<OptionValueRequest> optionValueList) {
        return new OrderFailedEvent(optionValueList);
    }
}
