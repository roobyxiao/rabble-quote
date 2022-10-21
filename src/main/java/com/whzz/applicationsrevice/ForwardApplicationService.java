package com.whzz.applicationsrevice;

import com.whzz.domain.bo.Forward;
import com.whzz.domain.service.ForwardDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ForwardApplicationService {
    private final ForwardDomainService domainService;

    public void saveAll(List<Forward> forwards) {
        domainService.saveAll(forwards);
    }

    public void deleteAll(List<Forward> forwards) {
        domainService.deleteAll(forwards);
    }

    public List<Forward> findByCodeOrderByDateDesc(String code) {
        return domainService.findByCodeOrderByDateDesc(code);
    }
}
