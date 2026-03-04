package com.trade.service;

import com.trade.dto.CreateOrderDTO;
import com.trade.dto.OrderDTO;
import com.trade.dto.UpdateOrderDTO;
import com.trade.exceptions.OrderNotFoundException;
import com.trade.mapper.OrderMapper;
import com.trade.model.Order;
import com.trade.model.OrderBook;
import com.trade.model.OrderSide;
import com.trade.model.OrderStatus;
import com.trade.repository.OrderBookRepository;
import com.trade.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;;
    private final OrderMapper orderMapper;
    private final MatchingService matchingService;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, MatchingService matchingService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.matchingService = matchingService;
    }
    //create, read, update, delete
    @Transactional
    public OrderDTO createOrder(CreateOrderDTO orderDTO) {
        OrderBook book = orderMapper.findOrCreateBook(orderDTO.getInstrument());
        Order order = orderMapper.toOrder(orderDTO, book);
        orderRepository.save(order);
        logger.info("Order created");
        matchingService.matchOrders(orderDTO.getInstrument());
        return orderMapper.toOrderDTO(order);
    }

    @Transactional
    public OrderDTO updateOrder(Long id, UpdateOrderDTO orderDTO) {
        Order toUpdate = orderRepository.getOrderById(id);
        if (toUpdate == null) throw new OrderNotFoundException("Order not found");
        if (toUpdate.getStatus() == OrderStatus.PENDING || toUpdate.getStatus() ==OrderStatus.PARTIALLY_FILLED) {
            if (orderDTO.getPrice() != null && orderDTO.getPrice() > 0) {
                toUpdate.setPrice(orderDTO.getPrice());
            }
            if (orderDTO.getQuantity() > 0) {
                toUpdate.setQuantity(orderDTO.getQuantity());
                if (toUpdate.getRemainingQuantity() == 0) {
                    toUpdate.setStatus(OrderStatus.FILLED);
                }
                if (toUpdate.getRemainingQuantity() != toUpdate.getQuantity()) {
                    toUpdate.setStatus(OrderStatus.PARTIALLY_FILLED);
                }
            }
            logger.info("Order updated");
            orderRepository.save(toUpdate);
            matchingService.matchOrders(toUpdate.getInstrument());
        } else{
            logger.info("Order update failed");
        }
        return orderMapper.toOrderDTO(toUpdate);
    }

    @Transactional
    public OrderDTO cancelOrder(Long id) {
        Order order = orderRepository.getOrderById(id);
        if (order == null) throw new OrderNotFoundException("Order not found");
        if(!order.getStatus().equals(OrderStatus.FILLED)) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            logger.info("Order cancelled");
        } else {
            logger.info("Order already filled, can not be cancelled");
        }
        return orderMapper.toOrderDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderDTO)
                .toList();
    }

    public OrderDTO getOrderById(Long id){
        Order order =  orderRepository.getOrderById(id);
        if(order == null){
            throw new OrderNotFoundException("Order not found");
        }
        return orderMapper.toOrderDTO(order);
    }

    public List<OrderDTO> getOrdersByTrader(Long traderId){
        return orderRepository.getOrdersByTraderId(traderId)
                .stream()
                .map(orderMapper::toOrderDTO)
                .toList();
    }


}
