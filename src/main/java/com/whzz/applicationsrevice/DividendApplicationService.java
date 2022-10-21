package com.whzz.applicationsrevice;

import com.whzz.domain.bo.Dividend;
import com.whzz.domain.service.DividendDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DividendApplicationService {
    private final DividendDomainService domainService;

    public void saveAll(List<Dividend> dividends) {
        domainService.saveAll(dividends);
    }

    public List<Dividend> findByCode(String code) {
        return domainService.findByCode(code);
    }
}
