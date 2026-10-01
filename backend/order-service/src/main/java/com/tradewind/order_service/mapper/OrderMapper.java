package com.tradewind.order_service.mapper;

import org.springframework.stereotype.Component;

import com.tradewind.order_service.dto.CreateOrderRequest;
import com.tradewind.order_service.dto.OrderResponse;
import com.tradewind.order_service.entity.Order;

@Component 
public class OrderMapper {

    public Order toEntity(CreateOrderRequest request) {
        
        return new Order(
            request.symbol().toUpperCase(),
            request.side(),
            request.quantity(),
            request.limitPrice()
        );
    }

    public OrderResponse toResponse(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getSymbol(),
            order.getSide(),
            order.getQuantity(),
            order.getLimitPrice(),
            order.getStatus(),
            order.getCreatedAt()
        );
    }

}
