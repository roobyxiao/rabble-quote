package com.whzz.domain.service;

import com.whzz.domain.bo.Tick;
import com.whzz.domain.repository.TickRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TickDomainService {
    private final TickRepository repository;

    public void save(Tick tick) {
        repository.save(tick);
    }
}
