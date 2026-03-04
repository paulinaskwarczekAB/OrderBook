package com.trade.service;

import com.trade.dto.CreateExchangeDTO;
import com.trade.dto.ExchangeDTO;
import com.trade.mapper.ExchangeMapper;
import com.trade.model.Exchange;
import com.trade.model.Sort;
import com.trade.repository.ExchangeRepository;
import com.trade.repository.SortRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExchangeService Unit Tests")
class ExchangeServiceTest {

    @Mock private ExchangeRepository exchangeRepository;
    @Mock private ExchangeMapper exchangeMapper;
    @Mock private SortRepository sortRepository;

    @InjectMocks
    private ExchangeService exchangeService;

    private Exchange exchange;
    private ExchangeDTO exchangeDTO;
    private CreateExchangeDTO createDTO;

    @BeforeEach
    void setUp() {
        exchange = new Exchange("NYSE", "US", 0.001, 0.0008, 1000);
        exchangeDTO = new ExchangeDTO(1L, "NYSE", "US", 0.001, 0.0008, 1000, 0.0);
        createDTO = new CreateExchangeDTO("NYSE", "US", 0.001, 0.0008, 1000);
    }

    @Test
    @DisplayName("createExchange — saves and returns DTO")
    void createExchange_savesAndReturnsDTO() {
        when(exchangeMapper.toExchange(createDTO)).thenReturn(exchange);
        when(exchangeRepository.save(exchange)).thenReturn(exchange);
        when(exchangeMapper.toExchangeDTO(exchange)).thenReturn(exchangeDTO);

        ExchangeDTO result = exchangeService.createExchange(createDTO);

        assertThat(result.getName()).isEqualTo("NYSE");
        assertThat(result.getRegion()).isEqualTo("US");
        verify(exchangeRepository).save(exchange);
    }

    @Test
    @DisplayName("updateExchange — updates existing exchange")
    void updateExchange_updatesExistingExchange() {
        when(exchangeRepository.findById(1L)).thenReturn(Optional.of(exchange));
        when(exchangeRepository.save(exchange)).thenReturn(exchange);
        when(exchangeMapper.toExchangeDTO(exchange)).thenReturn(exchangeDTO);

        ExchangeDTO result = exchangeService.updateExchange(1L, createDTO);

        assertThat(result).isEqualTo(exchangeDTO);
        verify(exchangeRepository).save(exchange);
    }

    @Test
    @DisplayName("updateExchange — creates new when not found")
    void updateExchange_createsNew_whenNotFound() {
        when(exchangeRepository.findById(99L)).thenReturn(Optional.empty());
        when(exchangeMapper.toExchange(createDTO)).thenReturn(exchange);
        when(exchangeRepository.save(exchange)).thenReturn(exchange);
        when(exchangeMapper.toExchangeDTO(exchange)).thenReturn(exchangeDTO);

        ExchangeDTO result = exchangeService.updateExchange(99L, createDTO);

        assertThat(result).isEqualTo(exchangeDTO);
        verify(exchangeRepository).save(exchange);
    }

    @Test
    @DisplayName("getAllExchanges — returns mapped list")
    void getAllExchanges_returnsMappedList() {
        when(exchangeRepository.findAll()).thenReturn(List.of(exchange));
        when(exchangeMapper.toExchangeDTO(exchange)).thenReturn(exchangeDTO);

        List<ExchangeDTO> result = exchangeService.getAllExchanges();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("NYSE");
    }

    @Test
    @DisplayName("getExchangeById — returns DTO when found")
    void getExchangeById_returnsDTOWhenFound() {
        when(exchangeRepository.findById(1L)).thenReturn(Optional.of(exchange));
        when(exchangeMapper.toExchangeDTO(exchange)).thenReturn(exchangeDTO);

        ExchangeDTO result = exchangeService.getExchangeById(1L);

        assertThat(result).isEqualTo(exchangeDTO);
    }

    @Test
    @DisplayName("getExchangeById — returns null when not found")
    void getExchangeById_returnsNull_whenNotFound() {
        when(exchangeRepository.findById(99L)).thenReturn(Optional.empty());

        ExchangeDTO result = exchangeService.getExchangeById(99L);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("executeTrade — creates new Sort when not found")
    void executeTrade_createsNewSort_whenNotFound() {
        Sort sort = new Sort("US");
        when(sortRepository.findByRegion("US")).thenReturn(null);
        when(sortRepository.save(any())).thenReturn(sort);
        when(exchangeRepository.findByRegion("US")).thenReturn(List.of());

        String result = exchangeService.executeTrade("US", "AAPL", 100, 150.0);

        assertThat(result).isEqualTo("No exchange found");
        verify(sortRepository).save(any(Sort.class));
    }

    @Test
    @DisplayName("executeTrade — returns result when sort exists")
    void executeTrade_returnsResult_whenSortExists() {
        Sort sort = spy(new Sort("US"));
        when(sortRepository.findByRegion("US")).thenReturn(sort);
        when(exchangeRepository.findByRegion("US")).thenReturn(List.of());

        String result = exchangeService.executeTrade("US", "AAPL", 100, 150.0);

        assertThat(result).isEqualTo("No exchange found");
    }
}