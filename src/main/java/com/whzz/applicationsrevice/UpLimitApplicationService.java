package com.whzz.applicationsrevice;

import com.whzz.domain.bo.UpLimit;
import com.whzz.domain.service.UpLimitDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UpLimitApplicationService {
    private final UpLimitDomainService domainService;

    public void saveAll(List<UpLimit> upLimits) {
        domainService.saveAll(upLimits);
    }
}
