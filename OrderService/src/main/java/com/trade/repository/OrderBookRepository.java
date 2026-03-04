package com.trade.repository;

import com.trade.model.OrderBook;
import com.trade.model.OrderSide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {
    OrderBook findByInstrument(String instrument);
}
