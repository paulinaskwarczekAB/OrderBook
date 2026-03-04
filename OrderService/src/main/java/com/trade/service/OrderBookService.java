package com.trade.service;

import com.trade.dto.OrderBookDTO;
import com.trade.mapper.OrderMapper;
import com.trade.model.OrderBook;
import com.trade.repository.OrderBookRepository;
import com.trade.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderBookService {

    private static final Logger logger = LoggerFactory.getLogger(OrderBookService.class);
    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final OrderMapper orderMapper;


    public OrderBookService(OrderBookRepository orderBookRepository,  OrderRepository orderRepository,OrderMapper orderMapper) {
        this.orderBookRepository = orderBookRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    //create, update, get, delete
    public OrderBook createOrderBook(OrderBook orderBook) {
        if(orderBookRepository.findByInstrument(orderBook.getInstrument()) == null) {
            orderBookRepository.save(orderBook);
            logger.info("OrderBook has been saved successfully");
            return orderBook;
        }
        logger.info("OrderBook already exists");
        return null;
    }

    public OrderBook updateOrderBook(String instrument, OrderBook orderBook) {
        OrderBook book = orderBookRepository.findByInstrument(instrument);
        if(book == null) {
            logger.info("OrderBook does not exist");
            OrderBook newBook = createOrderBook(new OrderBook(instrument));
            logger.info("OrderBook has been saved successfully");
            orderBookRepository.save(newBook);
            return newBook;
        }
        book.setBuyOrders(orderBook.getBuyOrders());
        book.setSellOrders(orderBook.getSellOrders());
        orderBookRepository.save(book);
        return book;
    }

    public List<OrderBookDTO> getAllOrderBooks() {
        return orderBookRepository.findAll()
                .stream()
                .map(orderMapper::toOrderBookDTO)
                .toList();
    }

    public OrderBookDTO getOrderBook(String instrument) {
        return orderMapper.toOrderBookDTO(orderBookRepository.findByInstrument(instrument));
    }
}
