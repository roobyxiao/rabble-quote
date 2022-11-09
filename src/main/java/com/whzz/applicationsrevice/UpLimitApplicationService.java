package com.whzz.applicationsrevice;

import com.whzz.domain.bo.UpLimit;
import com.whzz.domain.service.UpLimitDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UpLimitApplicationService {
    private final UpLimitDomainService domainService;

    public Optional<UpLimit> findById(String code, LocalDate date) {
        return domainService.findById(code, date);
    }

    public List<UpLimit> findByCode(String code) {
        return domainService.findByCode(code);
    }

    public void saveAll(List<UpLimit> upLimits) {
        domainService.saveAll(upLimits);
    }
}
