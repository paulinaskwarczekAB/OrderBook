package com.trade.dto;

import com.trade.model.OrderSide;
import com.trade.model.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@Getter
public class OrderDTO {
    private Long id;
    private Long traderId;
    private String instrument;
    private String name;
    private String region;
    private OrderSide side;
    private Double price;
    private OrderStatus status;
    private int quantity;
    private int remainingQuantity;


    public OrderDTO(Long id, Long traderId, String instrument, String name, String region,
                    OrderSide side, Double price, OrderStatus status, int quantity, int remainingQuantity) {
        this.id = id;
        this.traderId = traderId;
        this.instrument = instrument;
        this.name = name;
        this.region = region;
        this.side = side;
        this.price = price;
        this.status = status;
        this.quantity = quantity;
        this.remainingQuantity = remainingQuantity;
    }


    public void setQuantity(int quantity) { this.quantity = quantity; }
}
