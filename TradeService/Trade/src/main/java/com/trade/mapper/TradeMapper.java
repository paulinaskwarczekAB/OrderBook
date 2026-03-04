package com.trade.mapper;

import com.trade.dto.TradeDTO;
import com.trade.kafka.TradeEvent;
import com.trade.model.Trade;
import org.springframework.stereotype.Component;

@Component
public class TradeMapper {

    public TradeDTO toTradeDTO(Trade trade) {
        return new TradeDTO(
                trade.getId(),
                trade.getBuyOrderId(),
                trade.getSellOrderId(),
                trade.getBuyTraderId(),
                trade.getSellTraderId(),
                trade.getInstrument(),
                trade.getPrice(),
                trade.getQuantity(),
                trade.getExecutedAt()
        );
    }

    public Trade toTrade(TradeDTO tradeDTO) {
        return new Trade(
                tradeDTO.getBuyOrderId(),
                tradeDTO.getSellOrderId(),
                tradeDTO.getBuyTraderId(),
                tradeDTO.getSellTraderId(),
                tradeDTO.getInstrument(),
                tradeDTO.getPrice(),
                tradeDTO.getQuantity()
        );
    }

    public Trade toTrade(TradeEvent event) {
        return new Trade(
                event.getBuyOrderId(),
                event.getSellOrderId(),
                event.getBuyTraderId(),
                event.getSellTraderId(),
                event.getInstrument(),
                event.getPrice(),
                event.getQuantity()
        );

    }
}
