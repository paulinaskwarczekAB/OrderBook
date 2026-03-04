package com.trade.mapper;

import com.trade.dto.CreateOrderDTO;
import com.trade.dto.OrderBookDTO;
import com.trade.dto.OrderDTO;
import com.trade.kafka.TradeEvent;
import com.trade.model.Order;
import com.trade.model.OrderBook;
import com.trade.model.OrderSide;
import com.trade.model.OrderStatus;
import com.trade.repository.OrderBookRepository;
import com.trade.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;


@Component
public class OrderMapper {

    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;

    public OrderMapper(OrderRepository orderRepository, OrderBookRepository orderBookRepository) {
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
    }

    public Order toOrder(CreateOrderDTO request, OrderBook book) {
        return Order.builder()
                .orderBook(book)
                .traderId(request.getTraderId())
                .region(request.getRegion())
                .instrument(request.getInstrument())
                .side(request.getSide())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .status(OrderStatus.PENDING)
                .build();
    }

    public OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .traderId(order.getTraderId())
                .instrument(order.getInstrument())
                .region(order.getRegion())
                .side(order.getSide())
                .price(order.getPrice())
                .quantity(order.getQuantity())
                .remainingQuantity(order.getRemainingQuantity())
                .status(order.getStatus())
                .build();
    }


    public OrderBookDTO toOrderBookDTO(OrderBook book) {
        List<Order> allOrders = orderRepository.getOrdersByInstrument(book.getInstrument());

        List<OrderDTO> buys = allOrders.stream()
                .filter(o -> o.getSide() == OrderSide.BUY)
                .filter(o -> o.getStatus() == OrderStatus.PENDING || o.getStatus() == OrderStatus.PARTIALLY_FILLED)
                .sorted(Comparator.comparing(Order::getPrice).reversed())
                .map(this::toOrderDTO)
                .toList();

        List<OrderDTO> sells = allOrders.stream()
                .filter(o -> o.getSide() == OrderSide.SELL)
                .filter(o -> o.getStatus() == OrderStatus.PENDING || o.getStatus() == OrderStatus.PARTIALLY_FILLED)
                .sorted(Comparator.comparing(Order::getPrice))
                .map(this::toOrderDTO)
                .toList();

        return OrderBookDTO.builder()
                .instrument(book.getInstrument())
                .activeBuyOrders(buys)
                .activeSellOrders(sells)
                .build();
    }

    public TradeEvent toTradeEvent(Order buy, Order sell, int quantity, double price){
        return new TradeEvent(
                buy.getId(), sell.getId(),
                buy.getTraderId(), sell.getTraderId(),
                buy.getInstrument(), price, quantity);
    }


    public OrderBook findOrCreateBook(String instrument) {
        OrderBook book = orderBookRepository.findByInstrument(instrument.toLowerCase());
        if (book == null) {
            book = new OrderBook(instrument.toLowerCase());
            orderBookRepository.save(book);
        }
        return book;
    }
}
