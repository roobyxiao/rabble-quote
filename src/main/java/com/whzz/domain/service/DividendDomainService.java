package com.whzz.domain.service;

import com.whzz.domain.bo.Dividend;
import com.whzz.domain.repository.DividendRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DividendDomainService {
    private final DividendRepository repository;

    public void saveAll(List<Dividend> dividends) {
        repository.saveAll(dividends);
    }

    public List<Dividend> findByCode(String code) {
        return repository.findByCodeOrderByDividendDateDesc(code);
    }
}
