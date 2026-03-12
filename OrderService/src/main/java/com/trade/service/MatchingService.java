package com.trade.service;

import com.trade.kafka.TradeEvent;
import com.trade.kafka.TradeEventProducer;
import com.trade.mapper.OrderMapper;
import com.trade.model.Order;
import com.trade.model.OrderSide;
import com.trade.model.OrderStatus;
import com.trade.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static java.lang.Math.min;

@Service
public class MatchingService {

    private static final Logger logger = LoggerFactory.getLogger(MatchingService.class);

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TradeEventProducer tradeEventProducer;

    public MatchingService(OrderRepository orderRepository, OrderMapper orderMapper, TradeEventProducer tradeEventProducer) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.tradeEventProducer = tradeEventProducer;
    }

    public void matchOrders(String instrument) {
        List<Order> buys = orderRepository.getOrdersByInstrument(instrument).stream()
                .filter(o -> o.getSide() == OrderSide.BUY)
                .filter(o -> o.getStatus() == OrderStatus.PENDING || o.getStatus() == OrderStatus.PARTIALLY_FILLED)
                .sorted(Comparator.comparing(Order::getPrice, Comparator.nullsLast(Comparator.naturalOrder()))
                        .reversed()
                        .thenComparing(Comparator.comparing(Order::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()))))
                .toList();

        List<Order> sells = orderRepository.getOrdersByInstrument(instrument).stream()
                .filter(o -> o.getSide() == OrderSide.SELL)
                .filter(o -> o.getStatus() == OrderStatus.PENDING || o.getStatus() == OrderStatus.PARTIALLY_FILLED)
                .sorted(Comparator.comparing(Order::getPrice, Comparator.nullsLast(Comparator.naturalOrder()))
                        .reversed()
                        .thenComparing(Comparator.comparing(Order::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()))))
                .toList();

        for (Order buy : buys) {
            for (Order sell : sells) {
                if(buy.getRemainingQuantity() == 0) break;
                if(sell.getRemainingQuantity() == 0) continue;
                if(buy.getPrice() >= sell.getPrice()) {
                    doTransaction(buy, sell);
                }
            }
        }
    }

    public void doTransaction(Order buy, Order sell) {
        int quantity = min(buy.getRemainingQuantity(), sell.getRemainingQuantity());
        double price = sell.getPrice();
        actualiseOrder(buy, quantity);
        actualiseOrder(sell, quantity);

        TradeEvent event = orderMapper.toTradeEvent(buy, sell, quantity, price);
        tradeEventProducer.sendTradeEvent(event);
        logger.info("Trade event was send");
    }

    private void actualiseOrder(Order order, int quantity) {
        order.setFilledQuantity(order.getFilledQuantity() + quantity);
        if(order.getRemainingQuantity() == 0) {
            order.setStatus(OrderStatus.FILLED);
        } else {
            order.setStatus(OrderStatus.PARTIALLY_FILLED);
        }
        orderRepository.save(order);
    }
}