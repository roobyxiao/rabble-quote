package com.whzz.domain.service;

import com.whzz.domain.bo.Forward;
import com.whzz.domain.repository.ForwardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ForwardDomainService {
    private final ForwardRepository repository;

    public void saveAll(List<Forward> forwards) {
        repository.saveAll(forwards);
    }

    public void deleteAll(List<Forward> forwards) {
        repository.deleteAll(forwards);
    }

    public List<Forward> findByCodeOrderByDateDesc(String code) {
        return repository.findByCodeOrderByDateDesc(code);
    }
}
