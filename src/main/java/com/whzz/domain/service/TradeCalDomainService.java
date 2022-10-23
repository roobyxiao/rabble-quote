package com.whzz.domain.service;

import com.whzz.domain.bo.TradeCal;
import com.whzz.domain.repository.TradeCalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TradeCalDomainService {
    private final TradeCalRepository repository;

    public void saveAll(List<TradeCal> cals) {
        repository.saveAll(cals);
    }

    public boolean isOpen(LocalDate date) {
        return repository.findById(date).map(TradeCal::isOpen).orElse(false);
    }

    public List<TradeCal> findOpenCals(LocalDate startDate, LocalDate endDate) {
        return repository.findByDateBetweenAndOpenIsTrue(startDate, endDate);
    }

    public TradeCal findOpenDayBefore(TradeCal cal) {
        var lastCal = repository.findFirstByOpenIsTrueAndDateBeforeOrderByDateDesc(cal.getDate());
        return lastCal.orElse(null);
    }
}
