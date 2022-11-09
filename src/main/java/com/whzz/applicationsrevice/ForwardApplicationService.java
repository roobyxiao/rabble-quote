package com.whzz.applicationsrevice;

import com.whzz.domain.bo.Forward;
import com.whzz.domain.service.ForwardDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ForwardApplicationService {
    private final ForwardDomainService domainService;

    public void insertAll(List<Forward> forwards) {
        domainService.insertAll(forwards);
    }

    public void updateAll(List<Forward> forwards) {
        domainService.updateAll(forwards);
    }

    public void deleteAll(List<Forward> forwards) {
        domainService.deleteAll(forwards);
    }

    public List<Forward> findByCodeAndDateLessThan(String code, LocalDate date) {
        return domainService.findByCodeAndDateLessThan(code, date);
    }

    public List<Forward> findByCodeAndDateGreaterThanEqual(String code, LocalDate date) {
        return domainService.findByCodeAndDateGreaterThanEqual(code, date);
    }

    public List<Forward> findByCodeOrderByDate(String code) {
        return domainService.findByCodeOrderByDate(code);
    }

    public List<Forward> findByCodeOrderByDateDesc(String code) {
        return domainService.findByCodeOrderByDateDesc(code);
    }
}
