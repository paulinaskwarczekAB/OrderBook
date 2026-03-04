package com.trade.kafka;


import com.trade.mapper.TradeMapper;
import com.trade.model.Trade;
import com.trade.repository.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class TradeEventConsumer {

     private static final Logger logger = LoggerFactory.getLogger(TradeEventConsumer.class);
     private final TradeRepository tradeRepository;
     private final TradeMapper tradeMapper;

     public TradeEventConsumer(TradeRepository tradeRepository, TradeMapper tradeMapper) {
         this.tradeRepository = tradeRepository;
         this.tradeMapper = tradeMapper;
     }

     @KafkaListener(topics = "trade-executed", groupId = "trade-service-group")
     public void onTradeExecuted(TradeEvent event) {
         logger.info("Received trade event: {}", event);
         Trade trade = tradeMapper.toTrade(event);
         tradeRepository.save(trade);
         logger.info("Trade saved: {}", trade);
     }

}
