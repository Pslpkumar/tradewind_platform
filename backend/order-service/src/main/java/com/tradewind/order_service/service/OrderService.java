package com.tradewind.order_service.service;
import java.util.UUID;

import com.tradewind.order_service.dto.CreateOrderRequest;
import com.tradewind.order_service.dto.OrderResponse;
public interface OrderService {

    OrderResponse placeOrder(CreateOrderRequest request);

    OrderResponse getOrderById(UUID orderId);

}
