package com.trade.dto;

import com.trade.model.Exchange;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class ExchangeBookDTO {
    private Long id;
    private String region;
    private List<ExchangeDTO> exchanges;
}
