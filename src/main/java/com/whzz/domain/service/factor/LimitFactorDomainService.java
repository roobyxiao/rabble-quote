package com.whzz.domain.service.factor;

import com.whzz.domain.bo.Daily;
import com.whzz.domain.bo.factor.LimitFactor;
import com.whzz.domain.repository.factor.DailyFactorRepository;
import com.whzz.domain.repository.factor.LimitFactorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class LimitFactorDomainService {
    private final LimitFactorRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertAll(List<LimitFactor> factors) {
        factors.forEach(entityManager::persist);
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void updateAll(List<LimitFactor> factors) {
        factors.forEach(entityManager::merge);
        entityManager.flush();
        entityManager.clear();
    }
}
