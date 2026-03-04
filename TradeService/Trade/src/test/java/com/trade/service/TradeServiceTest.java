package com.trade.service;

import com.trade.dto.TradeDTO;
import com.trade.mapper.TradeMapper;
import com.trade.model.Trade;
import com.trade.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TradeService Unit Tests")
class TradeServiceTest {

    @Mock private TradeRepository tradeRepository;
    @Mock private TradeMapper tradeMapper;

    @InjectMocks
    private TradeService tradeService;

    private Trade trade;
    private TradeDTO tradeDTO;

    @BeforeEach
    void setUp() {
        trade = new Trade(1L, 2L, 10L, 20L, "AAPL", 150.0, 100);
        tradeDTO = new TradeDTO(1L, 1L, 2L, 10L, 20L, "AAPL", 150.0, 100, LocalDateTime.now());
    }

    @Test
    @DisplayName("findAllTrades — returns mapped list")
    void findAllTrades_returnsMappedList() {
        when(tradeRepository.findAll()).thenReturn(List.of(trade));
        when(tradeMapper.toTradeDTO(trade)).thenReturn(tradeDTO);

        List<TradeDTO> result = tradeService.findAllTrades();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getInstrument()).isEqualTo("AAPL");
    }

    @Test
    @DisplayName("findTradesByInstrument — returns filtered list")
    void findTradesByInstrument_returnsFilteredList() {
        when(tradeRepository.findByInstrument("AAPL")).thenReturn(List.of(trade));
        when(tradeMapper.toTradeDTO(trade)).thenReturn(tradeDTO);

        List<TradeDTO> result = tradeService.findTradesByInstrument("AAPL");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getInstrument()).isEqualTo("AAPL");
        verify(tradeRepository).findByInstrument("AAPL");
    }

    @Test
    @DisplayName("findAllTradesByTraderId — returns trades for trader")
    void findAllTradesByTraderId_returnsCorrectTrades() {
        when(tradeRepository.findByBuyTraderIdOrSellTraderId(10L, 10L)).thenReturn(List.of(trade));
        when(tradeMapper.toTradeDTO(trade)).thenReturn(tradeDTO);

        List<TradeDTO> result = tradeService.findAllTradesByTraderId(10L);

        assertThat(result).hasSize(1);
        verify(tradeRepository).findByBuyTraderIdOrSellTraderId(10L, 10L);
    }

    @Test
    @DisplayName("findTradeById — returns correct trade")
    void findTradeById_returnsCorrectTrade() {
        when(tradeRepository.findById(1L)).thenReturn(Optional.of(trade));
        when(tradeMapper.toTradeDTO(trade)).thenReturn(tradeDTO);

        TradeDTO result = tradeService.findTradeById(1L);

        assertThat(result).isEqualTo(tradeDTO);
        assertThat(result.getPrice()).isEqualTo(150.0);
    }

    @Test
    @DisplayName("findTradeById — throws when not found")
    void findTradeById_throws_whenNotFound() {
        when(tradeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tradeService.findTradeById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Trade not found");
    }
}