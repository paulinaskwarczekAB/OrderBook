package com.trade.kafka;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TradeEvent {
    private Long buyOrderId;
    private Long sellOrderId;
    private Long buyTraderId;
    private Long sellTraderId;
    private String instrument;
    private Double price;
    private int quantity;
    private LocalDateTime executedAt;


    public TradeEvent(Long buyOrderId, Long sellOrderId, Long buyTraderId,
                      Long sellTraderId, String instrument, Double price, int quantity) {
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
