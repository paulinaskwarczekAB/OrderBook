package com.trade.dto;

import com.trade.model.OrderSide;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

// DTO do tworzenia nowego zlecenia — to co klient wysyła DO nas (request body)
// @NotNull, @NotBlank itp. to walidacja — Spring automatycznie sprawdzi te warunki
@Getter
@NoArgsConstructor
public class CreateOrderDTO {

    @NotNull
    private Long traderId;

    @NotBlank
    private String instrument;

    @NotBlank
    private String region;

    @NotNull
    private OrderSide side;

    @NotNull
    @Min(value = 0)
    private Double price;

    @Min(value = 1)
    private int quantity;
}