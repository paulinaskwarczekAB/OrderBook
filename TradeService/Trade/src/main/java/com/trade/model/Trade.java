package com.trade.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long buyOrderId;
    private Long sellOrderId;
    private Long buyTraderId;
    private Long sellTraderId;

    private String instrument;
    private Double price;
    private int quantity;
    private LocalDateTime executedAt;

    public Trade(Long buyOrderId, Long sellOrderId, Long buyTraderId, Long sellTraderId, String instrument,
                 Double price, int quantity) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyTraderId = buyTraderId;
        this.sellTraderId = sellTraderId;
        this.instrument = instrument;
        this.price = price;
        this.quantity = quantity;
        this.executedAt = LocalDateTime.now();
    }

}
