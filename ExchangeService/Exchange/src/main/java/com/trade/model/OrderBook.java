package com.trade.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "exchange-order-books")
public class OrderBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String instrument;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_order_book_id")
    private List<MarketOrder> orders;

    public OrderBook(String instrument) {
        this.instrument = instrument;
        this.orders = new ArrayList<>();
    }

    public void addOrder(MarketOrder order) {
        this.orders.add(order);
    }

    public boolean hasLiquidity() {
        return orders.stream().anyMatch(o ->o.getSide() == OrderSide.SELL && o.getRemainingQuantity() > 0);
    }

    public double getBestAsk(){
        return orders.stream()
                .filter(o -> o.getSide() == OrderSide.SELL && o.getRemainingQuantity() > 0)
                .mapToDouble(MarketOrder::getPrice)
                .min().orElse(Double.MAX_VALUE);
    }

    public double getBestBid(){
        return orders.stream()
                .filter(o -> o.getSide() == OrderSide.BUY && o.getRemainingQuantity() > 0)
                .mapToDouble(MarketOrder::getPrice)
                .max().orElse(0.0);

    }

}
