package com.whzz.domain.service;

import com.whzz.domain.bo.Daily;
import com.whzz.domain.bo.DailyId;
import com.whzz.domain.repository.DailyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DailyDomainService {

    private final DailyRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertAll(List<Daily> dailies) {
        dailies.forEach(entityManager::persist);
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void updateAll(List<Daily> dailies) {
        dailies.forEach(entityManager::merge);
        entityManager.flush();
        entityManager.clear();
    }

    public void updateLimit(String code, LocalDate date, float limitUp, float limitDown) {
        repository.updateLimit(code, date, limitUp, limitDown);
    }

    public List<Daily> findByCode(String code) {
        return repository.findByCodeOrderByDate(code);
    }

    public List<Daily> findByCodeAndDateLessThan(String code, LocalDate date) {
        return repository.findByCodeAndDateLessThanOrderByDate(code, date);
    }

    public List<Daily> findByCodeAndDateGreaterThanEqual(String code, LocalDate startDate) {
        return repository.findByCodeAndDateGreaterThanEqualOrderByDate(code, startDate);
    }

    public Optional<Daily> findById(String code, LocalDate date) {
        return repository.findById(DailyId.builder().code(code).date(date).build());
    }

}
