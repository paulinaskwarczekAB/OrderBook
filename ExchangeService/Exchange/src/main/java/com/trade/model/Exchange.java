package com.trade.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "exchange")
@NoArgsConstructor
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String region;
    private Double feeLadder;
    private Double bulkFeeLadder;
    private int bulkThreshold;

    private Double currentDayTotalTradedValue;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id")
    private List<OrderBook> orderBooks;

    public Exchange(String name, String region, Double feeLadder, Double bulkFeeLadder, int bulkThreshold) {
        this.name = name;
        this.region = region;
        this.feeLadder = feeLadder;
        this.bulkFeeLadder = bulkFeeLadder;
        this.bulkThreshold = bulkThreshold;
        this.currentDayTotalTradedValue = 0.0;
        this.orderBooks = new ArrayList<>();
    }

    public Double calculateFee(int quantity, double price) {
        double result = price * quantity;
        if(quantity >= bulkThreshold) {
            return result * bulkFeeLadder;
        } else {
            return result * feeLadder;
        }
    }

    public void addTradedValue(int quantity, double price) {
        currentDayTotalTradedValue += (quantity * price);
    }

    public void setCurrentDayTotalTradedValue(Double value) { this.currentDayTotalTradedValue = value; }

    public void update(String name, String region, Double feeLadder, Double bulkFeeLadder, int bulkThreshold) {
        this.name          = name;
        this.region        = region;
        this.feeLadder     = feeLadder;
        this.bulkFeeLadder = bulkFeeLadder;
        this.bulkThreshold = bulkThreshold;
    }
}
