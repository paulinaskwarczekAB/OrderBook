package com.trade.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TradeEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(TradeEventProducer.class);

    static final String TOPIC = "trade-executed";
    private final KafkaTemplate<String, TradeEvent> kafkaTemplate;

    public TradeEventProducer(KafkaTemplate<String, TradeEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTradeEvent(TradeEvent tradeEvent) {
        kafkaTemplate.send(TOPIC, tradeEvent);
        logger.info("Sent TradeEvent to Topic {}", TOPIC);
    }
}
