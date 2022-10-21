package com.whzz.domain.service;

import com.whzz.domain.bo.UpLimit;
import com.whzz.domain.repository.UpLimitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UpLimitDomainService {
    private final UpLimitRepository repository;

    public void saveAll(List<UpLimit> upLimits) {
        repository.saveAll(upLimits);
    }
}
