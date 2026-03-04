package com.trade.mapper;

import com.trade.dto.CreateExchangeDTO;
import com.trade.dto.ExchangeDTO;
import com.trade.model.Exchange;
import org.springframework.stereotype.Component;

@Component
public class ExchangeMapper {

    public Exchange toExchange(CreateExchangeDTO exchange) {
        return new Exchange(
                exchange.getName(),
                exchange.getRegion(),
                exchange.getFeeLadder(),
                exchange.getBulkFeeLadder(),
                exchange.getBulkThreshold()
        );
    }

    public ExchangeDTO toExchangeDTO(Exchange exchange) {
        return new ExchangeDTO(
                exchange.getId(),
                exchange.getName(),
                exchange.getRegion(),
                exchange.getFeeLadder(),
                exchange.getBulkFeeLadder(),
                exchange.getBulkThreshold(),
                exchange.getCurrentDayTotalTradedValue()
        );
    }
}
