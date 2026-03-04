package com.trade.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "exchange_order")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderSide side;
    private Double price;
    private int quantity;
    private int filledQuantity;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(OrderSide side, Double price, int quantity) {
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.status = OrderStatus.PENDING;
    }


    public void setStatus(OrderStatus status) { this.status = status; }
    public int getRemainingQuantity() {
        return quantity - filledQuantity;
    }
}
