package com.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookDTO {

    private String region;
    private String instrument;
    private List<OrderDTO> activeBuyOrders;
    private List<OrderDTO> activeSellOrders;

    public OrderBookDTO(String region, String instrument) {
        this.region = region;
        this.instrument = instrument;
        this.activeBuyOrders = new ArrayList<>();
        this.activeSellOrders = new ArrayList<>();
    }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getInstrument() { return instrument; }

    public List<OrderDTO> getActiveBuyOrders() { return activeBuyOrders; }
    public void setActiveBuyOrders(List<OrderDTO> orders) { this.activeBuyOrders = orders; }

    public List<OrderDTO> getActiveSellOrders() { return activeSellOrders; }
    public void setActiveSellOrders(List<OrderDTO> orders) { this.activeSellOrders = orders; }
}
