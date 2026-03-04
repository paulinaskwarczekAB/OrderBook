package com.trade.repository;

import com.trade.model.ExchangeBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeBookRepository extends JpaRepository<ExchangeBook,Long> {
    ExchangeBook findByRegion(String region);
}
