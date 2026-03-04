package com.trade.repository;

import com.trade.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange,Long> {
    Exchange findByName(String name);
    List<Exchange> findByRegion(String region);
}
