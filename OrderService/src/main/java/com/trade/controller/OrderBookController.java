package com.trade.controller;

import com.trade.dto.OrderBookDTO;
import com.trade.mapper.OrderMapper;
import com.trade.repository.OrderBookRepository;
import com.trade.repository.OrderRepository;
import com.trade.service.OrderBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-books")
@CrossOrigin(origins = "*")
public class OrderBookController {

    private static final Logger logger = LoggerFactory.getLogger(OrderBookController.class);
    private final OrderBookService orderBookService;

    public OrderBookController(OrderBookService orderBookService) {
        this.orderBookService = orderBookService;
    }

    @GetMapping("/{instrument}")
    public ResponseEntity<OrderBookDTO> getOrderBook(@PathVariable String instrument) {
        return ResponseEntity.ok(orderBookService.getOrderBook(instrument));
    }

    @GetMapping
    public ResponseEntity<List<OrderBookDTO>> getAllOrderBooks() {
        return ResponseEntity.ok(orderBookService.getAllOrderBooks());
    }

}
