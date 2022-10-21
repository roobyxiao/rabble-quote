package com.whzz.domain.repository;

import com.whzz.domain.bo.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, String> {
    List<Stock> findByActive(boolean active);
}
