package com.whzz.domain.service;

import com.whzz.domain.bo.Daily;
import com.whzz.domain.repository.DailyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DailyDomainService {

    private final DailyRepository repository;
    public void saveAll(List<Daily> dailies) {
        repository.saveAll(dailies);
    }

    public void updateLimit(String code, LocalDate date, float limitUp, float limitDown) {
        repository.updateLimit(code, date, limitUp, limitDown);
    }

    public List<Daily> findByCode(String code) {
        return repository.findByCode(code);
    }

    public Optional<Daily> findByCodeAndDate(String code, LocalDate date) {
        return repository.findByCodeAndDate(code, date);
    }

    public boolean dailyExists(String code, LocalDate date) {
        return repository.existsByCodeAndDate(code, date);
    }
}
