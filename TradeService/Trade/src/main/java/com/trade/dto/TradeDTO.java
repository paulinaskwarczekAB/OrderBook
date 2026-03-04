package com.trade.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Data
@Getter
public class TradeDTO {
    private Long id;

    private Long buyOrderId;
    private Long sellOrderId;
    private Long buyTraderId;
    private Long sellTraderId;
    private String instrument;
    private Double price;
    private int quantity;
    private LocalDateTime executedAt;

    public TradeDTO(Long id, Long buyOrderId, Long sellOrderId, Long buyTraderId, Long sellTraderId, String instrument,
                 Double price, int quantity, LocalDateTime executedAt) {
        this.id = id;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyTraderId = buyTraderId;
        this.sellTraderId = sellTraderId;
        this.instrument = instrument;
        this.price = price;
        this.quantity = quantity;
        this.executedAt = executedAt;
    }
}
