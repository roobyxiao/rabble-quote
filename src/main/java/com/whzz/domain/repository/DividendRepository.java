package com.whzz.domain.repository;

import com.whzz.domain.bo.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DividendRepository extends JpaRepository<Dividend, Long> {
    List<Dividend> findByCodeOrderByDividendDateDesc(String code);
}
