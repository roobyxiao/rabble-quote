package com.whzz.applicationsrevice;

import com.whzz.domain.bo.Daily;
import com.whzz.domain.bo.Dividend;
import com.whzz.domain.service.DailyDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DailyApplicationService {
    private final DailyDomainService domainService;

    public void saveAll(List<Daily> dailies) {
        domainService.saveAll(dailies);
    }

    public void updateLimits(List<Daily> dailies) {
        dailies.forEach(daily -> {
            domainService.updateLimit(daily.getCode(), daily.getDate(), daily.getLimitUp(), daily.getLimitDown());
        });
    }

    public List<Daily> findByCode(String code) {
        return domainService.findByCode(code);
    }

    public Optional<Daily> findByCodeAndDate(String code, LocalDate date) {
        return domainService.findByCodeAndDate(code, date);
    }

    public boolean dailyExists(String code, LocalDate date) {
        return domainService.dailyExists(code, date);
    }
}
