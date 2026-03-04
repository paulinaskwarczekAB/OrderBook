package com.trade;

import com.trade.dto.CreateOrderDTO;
import com.trade.dto.OrderDTO;
import com.trade.dto.UpdateOrderDTO;
import com.trade.exceptions.OrderNotFoundException;
import com.trade.kafka.TradeEventProducer;
import com.trade.mapper.OrderMapper;
import com.trade.model.*;
import com.trade.repository.OrderBookRepository;
import com.trade.repository.OrderRepository;
import com.trade.service.MatchingService;
import com.trade.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Unit Tests")
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private MatchingService matchingService;

    @InjectMocks
    private OrderService orderService;

    private Order pendingOrder;
    private OrderBook orderBook;
    private OrderDTO orderDTO;
    private CreateOrderDTO createOrderDTO;

    @BeforeEach
    void setUp() {
        orderBook = new OrderBook("AAPL");

        pendingOrder = Order.builder()
                .traderId(1L)
                .instrument("AAPL")
                .region("US")
                .side(OrderSide.BUY)
                .price(150.0)
                .quantity(100)
                .status(OrderStatus.PENDING)
                .build();

        orderDTO = OrderDTO.builder()
                .id(1L)
                .traderId(1L)
                .instrument("AAPL")
                .region("US")
                .side(OrderSide.BUY)
                .price(150.0)
                .quantity(100)
                .remainingQuantity(100)
                .status(OrderStatus.PENDING)
                .build();

        createOrderDTO = new CreateOrderDTO();
    }

    @Test
    @DisplayName("createOrder — saves order and triggers matching")
    void createOrder_savesOrderAndTriggersMatching() {
        when(orderMapper.findOrCreateBook(any())).thenReturn(orderBook);
        when(orderMapper.toOrder(any(), any())).thenReturn(pendingOrder);
        when(orderRepository.save(any())).thenReturn(pendingOrder);
        when(orderMapper.toOrderDTO(any())).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(createOrderDTO);

        assertThat(result).isEqualTo(orderDTO);
        verify(orderRepository).save(pendingOrder);
        verify(matchingService).matchOrders(any());
    }

    @Test
    @DisplayName("cancelOrder — sets status to CANCELLED")
    void cancelOrder_setsStatusCancelled() {
        when(orderRepository.getOrderById(1L)).thenReturn(pendingOrder);
        when(orderRepository.save(any())).thenReturn(pendingOrder);
        when(orderMapper.toOrderDTO(any())).thenReturn(orderDTO);

        orderService.cancelOrder(1L);

        assertThat(pendingOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        verify(orderRepository).save(pendingOrder);
    }

    @Test
    @DisplayName("cancelOrder — throws when order not found")
    void cancelOrder_throws_whenNotFound() {
        when(orderRepository.getOrderById(99L)).thenReturn(null);

        assertThatThrownBy(() -> orderService.cancelOrder(99L))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("Order not found");
    }

    @Test
    @DisplayName("cancelOrder — does not cancel already FILLED order")
    void cancelOrder_doesNotCancel_whenFilled() {
        pendingOrder.setStatus(OrderStatus.FILLED);
        when(orderRepository.getOrderById(1L)).thenReturn(pendingOrder);
        when(orderMapper.toOrderDTO(any())).thenReturn(orderDTO);

        orderService.cancelOrder(1L);

        assertThat(pendingOrder.getStatus()).isEqualTo(OrderStatus.FILLED);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("getOrderById — throws when not found")
    void getOrderById_throws_whenNotFound() {
        when(orderRepository.getOrderById(99L)).thenReturn(null);

        assertThatThrownBy(() -> orderService.getOrderById(99L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("getAllOrders — returns mapped list")
    void getAllOrders_returnsMappedList() {
        when(orderRepository.findAll()).thenReturn(List.of(pendingOrder));
        when(orderMapper.toOrderDTO(pendingOrder)).thenReturn(orderDTO);

        List<OrderDTO> result = orderService.getAllOrders();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(orderDTO);
    }

    @Test
    @DisplayName("getOrdersByTrader — returns orders for trader")
    void getOrdersByTrader_returnsCorrectOrders() {
        when(orderRepository.getOrdersByTraderId(1L)).thenReturn(List.of(pendingOrder));
        when(orderMapper.toOrderDTO(pendingOrder)).thenReturn(orderDTO);

        List<OrderDTO> result = orderService.getOrdersByTrader(1L);

        assertThat(result).hasSize(1);
        verify(orderRepository).getOrdersByTraderId(1L);
    }
}