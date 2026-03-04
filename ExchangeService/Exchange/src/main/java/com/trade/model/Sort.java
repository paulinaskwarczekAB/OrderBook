package com.trade.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "sort")
public class Sort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String region;
    @ManyToMany
    @JoinTable(name = "sort_exchange",
            joinColumns = @JoinColumn(name = "sort_id"),
            inverseJoinColumns = @JoinColumn(name = "exchange_id"))
    private List<Exchange> exchanges = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "sort_orderbook",
            joinColumns = @JoinColumn(name = "sort_id"),
            inverseJoinColumns = @JoinColumn(name = "order_book_id"))
    private List<OrderBook> orderBooks = new ArrayList<>();

    public Sort(String region) {
        this.region = region;;
    }

    public Exchange executeTrade(String instrument, int quantity, double price) {
        String internal = tryInternalCrossing(instrument, quantity, price);
        if (internal != null) return null;

        if (exchanges.isEmpty()) return null;

        return exchanges.stream()
                .filter(e -> e.getOrderBooks().stream()
                        .anyMatch(book -> book.getInstrument().equalsIgnoreCase(instrument) && book.hasLiquidity()))
                .min(Comparator.comparingDouble(e -> e.calculateFee(quantity, price)))
                .orElse(exchanges.stream()
                        .min(Comparator.comparingDouble(e -> e.calculateFee(quantity, price)))
                        .orElse(null));
    }

    private String tryInternalCrossing(String instrument, int quantity, double price) {
        for(OrderBook book : orderBooks){
            if(!book.getInstrument().equalsIgnoreCase(instrument)) continue;
            if(book.getBestAsk() <= price){
                return "Internal crossing";
            }
        }
        return null;
    }

    public void addExchange(Exchange e) { this.exchanges.add(e); }
    public void addOrderBook(OrderBook ob) { this.orderBooks.add(ob); }
}
