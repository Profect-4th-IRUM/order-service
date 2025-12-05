package com.irum.orderservice.global.exception.errorcode;

import com.irum.global.advice.exception.errorcode.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
    ORDER_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "order-service error"),
    PAYMENT_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "payment-service error"),
    PRODUCT_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "product-service error"),
    MEMBER_SERVICE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "member-service error"),
    JSON_PROCESSING_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "json processing exception"),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String errorClassName() {
        return this.name();
    }
}
