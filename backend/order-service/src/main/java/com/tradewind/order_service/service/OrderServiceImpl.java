package com.tradewind.order_service.service;
import java.util.UUID;
import com.tradewind.order_service.repository.OrderRepository;

import jakarta.transaction.Transactional;

import com.tradewind.order_service.mapper.OrderMapper;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tradewind.order_service.dto.CreateOrderRequest;
import com.tradewind.order_service.dto.OrderResponse;
import com.tradewind.order_service.entity.Order;
import com.tradewind.order_service.exception.OrderNotFoundException;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest request) {
        Order saved = orderRepository.save(orderMapper.toEntity(request));
        log.info("Order placed id={} symbol={} side={} qty={}",
                saved.getId(), saved.getSymbol(), saved.getSide(), saved.getQuantity());
        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional 
    public OrderResponse getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
