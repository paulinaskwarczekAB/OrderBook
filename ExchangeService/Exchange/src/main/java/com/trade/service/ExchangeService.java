package com.trade.service;


import com.trade.dto.CreateExchangeDTO;
import com.trade.dto.ExchangeDTO;
import com.trade.mapper.ExchangeMapper;
import com.trade.model.Exchange;
import com.trade.model.Sort;
import com.trade.repository.ExchangeBookRepository;
import com.trade.repository.ExchangeRepository;
import com.trade.repository.SortRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeService {

    Logger logger = LoggerFactory.getLogger(ExchangeService.class);

    private final ExchangeRepository exchangeRepository;
    private final ExchangeMapper exchangeMapper;
    private final SortRepository sortRepository;

    public ExchangeService(ExchangeRepository exchangeRepository, ExchangeMapper exchangeMapper,  SortRepository sortRepository) {
        this.exchangeRepository = exchangeRepository;
        this.exchangeMapper = exchangeMapper;
        this.sortRepository = sortRepository;
    }

    //create, update, get, delete
    public ExchangeDTO createExchange(CreateExchangeDTO exchangeDTO) {
        Exchange exchange = exchangeMapper.toExchange(exchangeDTO);
        exchangeRepository.save(exchange);
        logger.info("Exchange created");
        return exchangeMapper.toExchangeDTO(exchange);
    }

    public ExchangeDTO updateExchange(Long id, CreateExchangeDTO exchangeDTO) {
        Exchange exchange = exchangeRepository.findById(id).orElse(null);
        if (exchange == null) {
            logger.info("Exchange not found, creating exchange");
            return createExchange(exchangeDTO);
        }
        exchange.update(exchangeDTO.getName(), exchangeDTO.getRegion(),
                exchangeDTO.getFeeLadder(), exchangeDTO.getBulkFeeLadder(),
                exchangeDTO.getBulkThreshold());
        exchangeRepository.save(exchange);
        return exchangeMapper.toExchangeDTO(exchange);
    }

    public List<ExchangeDTO> getAllExchanges() {
        return exchangeRepository.findAll()
                .stream()
                .map(exchangeMapper::toExchangeDTO)
                .toList();
    }

    public List<ExchangeDTO> getExchangesByRegion(String region) {
        return exchangeRepository.findByRegion(region)
                .stream()
                .map(exchangeMapper::toExchangeDTO)
                .toList();
    }

    public ExchangeDTO getExchangeById(Long id) {
        Exchange exchange = exchangeRepository.findById(id).orElse(null);
        if (exchange == null) {
            logger.info("Exchange not found");
            return null;
        }
        return exchangeMapper.toExchangeDTO(exchange);
    }

    public String executeTrade(String region, String instrument, int quantity, Double price) {
        Sort sort = sortRepository.findByRegion(region);
        if (sort == null) {
            sort = new Sort(region);
            sortRepository.save(sort);
        }
        exchangeRepository.findByRegion(region).forEach(sort::addExchange);
        Exchange best = sort.executeTrade(instrument, quantity, price);
        if (best == null) return "No exchange found";
        best.addTradedValue(quantity, price);
        exchangeRepository.save(best);
        return "Trade executed on " + best.getName();
    }

}
