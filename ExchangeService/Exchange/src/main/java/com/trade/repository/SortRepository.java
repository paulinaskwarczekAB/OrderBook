package com.trade.repository;

import com.trade.model.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SortRepository extends JpaRepository<Sort, Long> {
    Sort findByRegion(String region);
}
