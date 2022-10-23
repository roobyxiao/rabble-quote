package com.whzz.domain.service;

import com.whzz.domain.bo.Forward;
import com.whzz.domain.repository.ForwardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ForwardDomainService {
    private final ForwardRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void insertAll(List<Forward> forwards) {
        forwards.forEach(entityManager::persist);
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void updateAll(List<Forward> forwards) {
        forwards.forEach(entityManager::merge);
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void deleteAll(List<Forward> forwards) {
        forwards.forEach(entityManager::remove);
        entityManager.flush();
        entityManager.clear();
    }

    public List<Forward> findByCodeOrderByDateDesc(String code) {
        return repository.findByCodeOrderByDateDesc(code);
    }
}
