package com.whzz.domain.service.factor;

import com.whzz.domain.bo.Forward;
import com.whzz.domain.bo.factor.DailyFactor;
import com.whzz.domain.bo.factor.ForwardFactor;
import com.whzz.domain.repository.factor.ForwardFactorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ForwardFactorDomainService {
    @PersistenceContext
    private EntityManager entityManager;
    private final ForwardFactorRepository repository;

    @Transactional
    public void insertAll(List<ForwardFactor> factors) {
        factors.forEach(entityManager::persist);
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void deleteAll(List<ForwardFactor> factors) {
        factors.forEach(entityManager::remove);
        entityManager.flush();
        entityManager.clear();
    }

    public List<ForwardFactor> findByCode(String code) {
        return repository.findByCode(code);
    }
}
