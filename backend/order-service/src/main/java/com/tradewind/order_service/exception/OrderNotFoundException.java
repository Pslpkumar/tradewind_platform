package com.tradewind.order_service.exception;

import java.util.UUID;

public class OrderNotFoundException extends BusinessException {

    public OrderNotFoundException(UUID id) {
        super(ErrorCode.ORDER_NOT_FOUND, "Order not found: " + id);
    }
}