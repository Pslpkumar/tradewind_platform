package com.tradewind.order_service.dto;

import java.math.BigDecimal;
import com.tradewind.order_service.enums.Side;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(
    @NotBlank(message = "Symbol is required")
    @Size(max = 12, message = "Symbol must not exceed 12 characters")
    String symbol,

    @NotBlank(message = "Side is required(BUY or SELL)")
    Side side,

    @NotBlank(message = "Quantity is required")
    @Size(message = "Quantity must be greater than 0")
    int quantity,

    @NotBlank(message = "Limit price is required")
    @Size(message = "Limit price must be greater than 0")
    BigDecimal limitPrice
) {
}
