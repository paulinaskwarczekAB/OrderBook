package com.trade.controller;


import com.trade.dto.TradeDTO;
import com.trade.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
@CrossOrigin(origins = "*")
public class TradeController {

    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);
    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping
    public ResponseEntity<List<TradeDTO>> findAll() {
        return ResponseEntity.ok(tradeService.findAllTrades());
    }

    @GetMapping("/instrument/{instrument}")
    public ResponseEntity<List<TradeDTO>> findByInstrument(@PathVariable String instrument) {
        return ResponseEntity.ok(tradeService.findTradesByInstrument(instrument));
    }

    @GetMapping("/trader/{traderId}")
    public ResponseEntity<List<TradeDTO>> findByTraderId(@PathVariable Long traderId) {
        return ResponseEntity.ok(tradeService.findAllTradesByTraderId(traderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TradeDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tradeService.findTradeById(id));
    }

}
