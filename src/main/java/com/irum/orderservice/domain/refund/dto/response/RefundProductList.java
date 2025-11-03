package com.irum.orderservice.domain.refund.dto.response;

import java.util.UUID;

public record RefundProductList(
        UUID orderDetailId,
        String productName,
        Integer quantity,
        Integer productPrice,
        String optionTitle) {}
