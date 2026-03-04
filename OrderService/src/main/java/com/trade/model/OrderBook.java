package com.trade.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_books")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String instrument;

    @OneToMany
    private List<Order> buyOrders;

    @OneToMany
    private List<Order> sellOrders;

    public OrderBook(String instrument) {
        this.instrument = instrument;
        this.buyOrders = new ArrayList<>();
        this.sellOrders = new ArrayList<>();
    }

    public Long getId() { return id; }
    public String getInstrument() { return instrument; }

    public List<Order> getBuyOrders() { return this.buyOrders; }
    public void setBuyOrders(List<Order> buyOrders) { this.buyOrders = buyOrders; }
    public List<Order> getSellOrders() { return this.sellOrders; }
    public void setSellOrders(List<Order> sellOrders) {this.sellOrders = sellOrders; }


}
