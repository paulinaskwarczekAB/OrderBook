package com.trade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class CreateExchangeDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String region;

    @NotNull
    @Positive
    private Double feeLadder;

    @NotNull
    @Positive
    private Double bulkFeeLadder;

    @Positive
    private int bulkThreshold;

    public CreateExchangeDTO(String name, String region, Double feeLadder, Double bulkFeeLadder, int bulkThreshold) {
        this.name = name;
        this.region = region;
        this.feeLadder = feeLadder;
        this.bulkFeeLadder = bulkFeeLadder;
        this.bulkThreshold = bulkThreshold;
    }

}
