package com.whzz.domain.repository;

import com.whzz.domain.bo.TradeCal;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TradeCalRepository extends JpaRepository<TradeCal, LocalDate> {
    Optional<TradeCal> findFirstByOpenIsTrueAndDateBeforeOrderByDateDesc(LocalDate date);

    List<TradeCal> findByDateBetweenAndOpenIsTrue(LocalDate startDate, LocalDate endDate);
}
