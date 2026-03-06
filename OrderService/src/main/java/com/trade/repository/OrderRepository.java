package com.trade.repository;

import com.trade.model.Order;
import com.trade.model.OrderSide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> getOrdersByInstrument(String instrument);
    List<Order> getOrdersByRegion(String region);
    List<Order> getOrdersByTraderId(Long traderId);
    Order getOrderById(Long orderId);
    List<Order> findByRegionAndSide(String region, OrderSide side);
}
