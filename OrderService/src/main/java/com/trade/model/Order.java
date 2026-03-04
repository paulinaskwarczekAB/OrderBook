package com.trade.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_book_id")
    private OrderBook orderBook;

    private Long traderId;
    private String region;
    private String instrument;

    @Enumerated(EnumType.STRING)
    private OrderSide side;
    private Double price;
    private int quantity;
    private int filledQuantity;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    LocalDateTime createdAt;

    public Order(OrderBook book, Long traderId, String region, String instrument, OrderSide side, Double price, int quantity) {
        this.orderBook = book;
        this.traderId = traderId;
        this.region = region;
        this.instrument = instrument;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }


    public void setPrice(Double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setFilledQuantity(int quantity) { this.filledQuantity = quantity; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public int getRemainingQuantity() {
        return quantity - filledQuantity;
    }
}
