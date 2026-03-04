package com.trade.controller;

import com.trade.dto.CreateOrderDTO;
import com.trade.dto.OrderDTO;
import com.trade.dto.UpdateOrderDTO;
import com.trade.model.Order;
import com.trade.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //post, put, get, delete
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order));
    }

    @PutMapping("/{orderId}/update")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long orderId, @RequestBody UpdateOrderDTO order) {
        return  ResponseEntity.ok(orderService.updateOrder(orderId, order));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));

    }

    @GetMapping("/trader/{traderId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByTrader(@PathVariable Long traderId) {
        return ResponseEntity.ok(orderService.getOrdersByTrader(traderId));
    }
}
