package com.tradewind.order_service.controller;
import java.net.URI;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tradewind.order_service.dto.CreateOrderRequest;
import com.tradewind.order_service.dto.OrderResponse;
import com.tradewind.order_service.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping ("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping("/{id}")
    public String getMethodName(@PathVariable("id") String id) {
        return orderService.getOrderById(UUID.fromString(id)).toString();
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse created = orderService.placeOrder(request);
        return ResponseEntity
                .created(URI.create("/api/v1/orders/" + created.id()))
                .body(created);
    }


}
