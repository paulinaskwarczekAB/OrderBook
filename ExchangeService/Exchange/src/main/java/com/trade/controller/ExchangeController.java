package com.trade.controller;

import com.trade.dto.CreateExchangeDTO;
import com.trade.dto.ExchangeDTO;
import com.trade.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exchanges")
@CrossOrigin(origins = "*")
public class ExchangeController {

    private ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @PostMapping
    public ResponseEntity<ExchangeDTO> createExchange(@RequestBody CreateExchangeDTO exchangeDTO) {
        return ResponseEntity.ok(exchangeService.createExchange(exchangeDTO));
    }

    @PutMapping("/{exchangeId}")
    public ResponseEntity<ExchangeDTO> updateExchange(@PathVariable Long exchangeId, @RequestBody CreateExchangeDTO exchangeDTO) {
        return ResponseEntity.ok(exchangeService.updateExchange(exchangeId, exchangeDTO));
    }

    @GetMapping("/{exchangeId}")
    public ResponseEntity<ExchangeDTO> getExchange(@PathVariable Long exchangeId) {
        ExchangeDTO dto = exchangeService.getExchangeById(exchangeId);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<List<ExchangeDTO>> getExchangeByRegion(@PathVariable String region) {
        return ResponseEntity.ok(exchangeService.getExchangesByRegion(region));
    }

    @GetMapping
    public ResponseEntity<List<ExchangeDTO>> getAllExchanges() {
        return ResponseEntity.ok(exchangeService.getAllExchanges());
    }

    @PostMapping("/execute/{region}")
    public ResponseEntity<String> executeTrade(@PathVariable String region, @RequestParam String instrument,
                                                             @RequestParam Double price, @RequestParam int quantity) {
        return ResponseEntity.ok(exchangeService.executeTrade(region, instrument, quantity, price));

    }
}
