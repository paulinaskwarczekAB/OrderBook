package com.trade.dto;

import lombok.Data;

@Data
public class ExchangeDTO {

    private Long id;
    private String name;
    private String region;
    private Double feeLadder;
    private Double bulkFeeLadder;
    private int bulkThreshold;
    private Double currentDaysTotalTradedValue;

    public ExchangeDTO(Long id, String name, String region, Double feeLadder, Double bulkFeeLadder, int bulkThreshold, double currentDaysTotalTradedValue) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.feeLadder = feeLadder;
        this.bulkFeeLadder = bulkFeeLadder;
        this.bulkThreshold = bulkThreshold;
        this.currentDaysTotalTradedValue = currentDaysTotalTradedValue;
    }
}
