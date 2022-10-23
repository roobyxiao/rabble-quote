package com.whzz.applicationsrevice;

import com.whzz.domain.bo.Daily;
import com.whzz.domain.service.DailyDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DailyApplicationService {
    private final DailyDomainService domainService;

    public void insertAll(List<Daily> dailies) {
        domainService.insertAll(dailies);
    }

    public void updateLimits(List<Daily> dailies) {
         domainService.updateAll(dailies.stream().map(daily -> {
            var dailyInDb = domainService.findById(daily.getCode(), daily.getDate());
            return dailyInDb.map(x -> {
                x.setLimitUp(daily.getLimitUp());
                x.setLimitDown(daily.getLimitDown());
                return x;
            }).orElse(null);
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public List<Daily> findByCode(String code) {
        return domainService.findByCode(code);
    }

    public Optional<Daily> findById(String code, LocalDate date) {
        return domainService.findById(code, date);
    }
}
