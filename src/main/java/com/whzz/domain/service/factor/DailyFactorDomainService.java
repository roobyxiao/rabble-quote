package com.whzz.domain.service.factor;

import com.whzz.domain.bo.Daily;
import com.whzz.domain.bo.factor.DailyFactor;
import com.whzz.domain.repository.factor.DailyFactorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class DailyFactorDomainService {
    private final DailyFactorRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertAll(List<DailyFactor> factors) {
        factors.forEach(entityManager::persist);
        entityManager.flush();
        entityManager.clear();
    }
}
