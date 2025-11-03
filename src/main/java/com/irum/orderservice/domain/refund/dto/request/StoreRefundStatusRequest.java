package com.irum.orderservice.domain.refund.dto.request;

import com.irum.come2us.domain.refund.domain.entity.enums.RefundStatus;
import jakarta.validation.constraints.NotNull;

public record StoreRefundStatusRequest(@NotNull RefundStatus refundStatus) {}
