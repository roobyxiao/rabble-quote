package com.whzz.applicationsrevice;

import com.whzz.domain.bo.TradeCal;
import com.whzz.domain.service.TradeCalDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class TradeCalApplicationService {
    private final TradeCalDomainService domainService;

    public void saveTradeCals(List<TradeCal> cals) {
        cals.sort(Comparator.comparing(TradeCal::getDate));
        var firstCal = cals.stream().filter(TradeCal::isOpen).findFirst();
        var lastTradeDay = firstCal.map(domainService::findOpenDayBefore).map(TradeCal::getDate).orElse(null);
        for (TradeCal cal: cals) {
            if (cal.isOpen()) {
                cal.setLastTradeDay(lastTradeDay);
                lastTradeDay = cal.getDate();
            }
        }
        domainService.saveAll(cals);
    }

    public boolean isOpen(LocalDate date) {
        return domainService.isOpen(date);
    }

    public List<TradeCal> findOpenCals(LocalDate startDate) {
        return findOpenCals(startDate, LocalDate.now());
    }

    public List<TradeCal> findOpenCals(LocalDate startDate, LocalDate endDate) {
        return domainService.findOpenCals(startDate, endDate);
    }
}
