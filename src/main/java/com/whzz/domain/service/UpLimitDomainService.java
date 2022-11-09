package com.whzz.domain.service;

import com.whzz.domain.bo.DailyId;
import com.whzz.domain.bo.UpLimit;
import com.whzz.domain.repository.UpLimitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpLimitDomainService {
    private final UpLimitRepository repository;

    public Optional<UpLimit> findById(String code, LocalDate date) {
        return repository.findById(DailyId.builder().code(code).date(date).build());
    }

    public List<UpLimit> findByCode(String code) {
        return repository.findByCode(code);
    }

    public void saveAll(List<UpLimit> upLimits) {
        repository.saveAll(upLimits);
    }
}
