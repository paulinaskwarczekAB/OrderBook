package com.trade.repository;

import com.trade.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByBuyTraderIdOrSellTraderId(Long buyTraderId, Long sellTraderId);
    List<Trade> findByInstrument(String instrument);
}
