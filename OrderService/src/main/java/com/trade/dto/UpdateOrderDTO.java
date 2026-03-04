package com.trade.dto;

import jakarta.validation.constraints.Min;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UpdateOrderDTO {

    @Min(value = 0)
    private Double price;
    @Min(value = 1)
    private int quantity;

    public UpdateOrderDTO(Double price, int quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public Double getPrice() { return price; }
    public int getQuantity() { return quantity; }
}
