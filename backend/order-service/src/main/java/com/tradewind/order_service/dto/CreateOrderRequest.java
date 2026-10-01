package com.tradewind.order_service.dto;

import java.math.BigDecimal;
import com.tradewind.order_service.enums.Side;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
    @NotBlank(message = "Symbol is required")
    @Size(max = 12, message = "Symbol must not exceed 12 characters")
    String symbol,

    @NotNull(message = "Side is required(BUY or SELL)")
    Side side,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    int quantity,

    @NotNull(message = "Limit price is required")
    @Positive(message = "Limit price must be greater than 0")
    BigDecimal limitPrice
) {
}
