package com.whzz.applicationsrevice.factor;

import com.whzz.applicationsrevice.ForwardApplicationService;
import com.whzz.domain.bo.Forward;
import com.whzz.domain.bo.factor.ForwardFactor;
import com.whzz.domain.service.factor.ForwardFactorDomainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ForwardFactorApplicationService {
    private ForwardApplicationService forwardApplicationService;
    private ForwardFactorDomainService domainService;

    public void reCalculateForwardFactor(String code, LocalDate date) {
        var factors = domainService.findByCode(code);
        domainService.deleteAll(factors);
        calculateForwardFactorByCode(code, date);
    }

    public void calculateForwardFactorByCode(String code, LocalDate startDate) {
        var beforeForwards = forwardApplicationService.findByCodeAndDateLessThan(code, startDate);
        var afterForwards = forwardApplicationService.findByCodeAndDateGreaterThanEqual(code, startDate);
        var factors = afterForwards.stream().map(forward -> {
            var factor = ForwardFactor.builder().code(forward.getCode()).date(forward.getDate()).build();
            beforeForwards.add(forward);
            factor.setMA5(getMA(beforeForwards, 5));
            factor.setMA10(getMA(beforeForwards, 10));
            factor.setMA20(getMA(beforeForwards, 20));
            factor.setMA59(getMA(beforeForwards, 59));
            factor.setMA60(getMA(beforeForwards, 60));
            factor.setMA249(getMA(beforeForwards, 249));
            factor.setMA250(getMA(beforeForwards, 250));
            factor.setVA5(getVA(beforeForwards, 5));
            factor.setVA10(getVA(beforeForwards, 10));
            factor.setVA20(getVA(beforeForwards, 20));
            factor.setVA60(getVA(beforeForwards, 60));
            factor.setVA250(getVA(beforeForwards, 250));
            return factor;
        }).filter(factor -> Objects.nonNull(factor.getMA5())).collect(Collectors.toList());
        domainService.insertAll(factors);
        log.warn("Processed forward factor "+ code +" ······");
    }

    private Float getMA(List<Forward> forwards, int day) {
        var reversed = forwards.stream().sorted(Comparator.comparing(Forward::getDate).reversed()).collect(Collectors.toList());
        if (reversed.size() >= day)
            return Double.valueOf(reversed.stream().limit(day)
                            .mapToDouble(Forward::getClose).average().getAsDouble())
                    .floatValue();
        return null;
    }

    private Long getVA(List<Forward> forwards, int day) {
        var reversed = forwards.stream().sorted(Comparator.comparing(Forward::getDate).reversed()).collect(Collectors.toList());
        if (reversed.size() >= day) {
            return Double.valueOf(reversed.stream().limit(day)
                            .mapToLong(forward -> Float.valueOf(forward.getVolume() * forward.getRatio()).longValue())
                            .average().getAsDouble())
                    .longValue();
        }
        return null;
    }
}
