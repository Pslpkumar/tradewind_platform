package com.tradewind.order_service.dto;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.Instant;
import com.tradewind.order_service.enums.OrderStatus;
import com.tradewind.order_service.enums.Side;

public record OrderResponse(
    UUID id,
    String symbol,
    Side side,
    int quantity,
    BigDecimal limitPrice,
    OrderStatus status,
    Instant createdAt
) {
}
