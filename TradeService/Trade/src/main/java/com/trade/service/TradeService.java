package com.trade.service;


import com.trade.dto.TradeDTO;
import com.trade.mapper.TradeMapper;
import com.trade.model.Trade;
import com.trade.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);
    private final TradeRepository tradeRepository;
    private final TradeMapper tradeMapper;

    public TradeService(TradeRepository tradeRepository, TradeMapper tradeMapper) {
        this.tradeRepository = tradeRepository;
        this.tradeMapper = tradeMapper;
    }

    //get all, by: id, instrument trader
    public List<TradeDTO> findAllTrades() {
        return tradeRepository.findAll().stream().map(tradeMapper::toTradeDTO).toList();
    }

    public List<TradeDTO> findTradesByInstrument(String instrument) {
        return tradeRepository.findByInstrument(instrument).stream().map(tradeMapper::toTradeDTO).toList();
    }

    public List<TradeDTO> findAllTradesByTraderId(Long traderId) {
        return tradeRepository.findByBuyTraderIdOrSellTraderId(traderId, traderId).stream().map(tradeMapper::toTradeDTO).toList();
    }

    public TradeDTO findTradeById(Long id) {
        return tradeRepository.findById(id)
                .map(tradeMapper::toTradeDTO)
                .orElseThrow(() -> new RuntimeException("Trade not found: " + id));
    }

}
