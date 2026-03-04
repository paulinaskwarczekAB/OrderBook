package com.trade.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "market_order")
@NoArgsConstructor
public class MarketOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderSide side;

    private Double price;
    private int quantity;
    private int filledQuantity;

    public MarketOrder(OrderSide side, Double price, int quantity) {
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.filledQuantity = 0;
    }


    public int getRemainingQuantity() {
        return quantity - filledQuantity;
    }
}