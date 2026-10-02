package com.tradewind.order_service.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    ORDER_NOT_FOUND("ORD-404", HttpStatus.NOT_FOUND, "Order not found"),
    VALIDATION_FAILED("ORD-400", HttpStatus.BAD_REQUEST, "Request validation failed"),
    MALFORMED_REQUEST("ORD-400-MALFORMED", HttpStatus.BAD_REQUEST, "Malformed request"),
    INTERNAL_ERROR("ORD-500", HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");

    private final String code;
    private final HttpStatus status;
    private final String title;

    ErrorCode(String code, HttpStatus status, String title) {
        this.code = code;
        this.status = status;
        this.title = title;
    }

    public String getCode() { return code; }
    public HttpStatus getStatus() { return status; }
    public String getTitle() { return title; }
}