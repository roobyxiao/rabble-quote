package com.whzz.applicationsrevice.factor;

import com.google.common.collect.Streams;
import com.whzz.applicationsrevice.DailyApplicationService;
import com.whzz.applicationsrevice.UpLimitApplicationService;
import com.whzz.domain.bo.Daily;
import com.whzz.domain.bo.UpLimit;
import com.whzz.domain.bo.factor.DailyFactor;
import com.whzz.domain.service.factor.DailyFactorDomainService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class DailyFactorApplicationService {
    private DailyApplicationService dailyApplicationService;
    private DailyFactorDomainService domainService;

    private UpLimitApplicationService upLimitApplicationService;

    public void calculateDailyFactorByCode(String code, LocalDate startDate) {
        var limits = upLimitApplicationService.findByCode(code);
        var beforeDailies = dailyApplicationService.findByCodeAndDateLessThan(code, startDate);
        var afterDailies = dailyApplicationService.findByCodeAndDateGreaterThanEqual(code, startDate);
        var factors = afterDailies.stream().map(daily -> {
            var factor = DailyFactor.builder().code(daily.getCode()).date(daily.getDate()).build();
            factor.setAmpPct(getAmpPct(daily));
            factor.setOpenPct(getOpenPct(daily));
            factor.setIntradayPct(getIntradayPct(daily));
            factor.setUpperShadow(getUpperShadow(daily));
            factor.setLowerShadow(getLowerShadow(daily));
            var monthPair = getLimitAndBroken(beforeDailies, limits, 20);
            factor.setMonthUp(monthPair.getLeft());
            factor.setMonthBroken(monthPair.getRight());
            var quarterPair = getLimitAndBroken(beforeDailies, limits, 60);
            factor.setQuarterUp(quarterPair.getLeft());
            factor.setQuarterBroken(quarterPair.getRight());
            var yearPair = getLimitAndBroken(beforeDailies, limits, 250);
            factor.setYearUp(yearPair.getLeft());
            factor.setYearBroken(yearPair.getRight());
            beforeDailies.add(daily);
            return factor;
        }).collect(Collectors.toList());
        domainService.insertAll(factors);
        log.warn("Processed daily factor "+ code +" ······");
    }

    private Pair<Integer, Integer> getLimitAndBroken(List<Daily> dailies, List<UpLimit> limits, int day) {
        var upCount = 0;
        int brokenCount = 0;
        if (dailies.size() > 0) {
            var reversed = dailies.stream()
                    .sorted(Comparator.comparing(Daily::getDate).reversed()).limit(day).collect(Collectors.toList());
            var limitCount = (int) reversed.stream().filter(daily -> daily.getLimitUp() == daily.getHigh()).count();
            var startDate = Streams.findLast(reversed.stream()).map(Daily::getDate).orElse(null);
            var endDate = reversed.stream().findFirst().map(Daily::getDate).orElse(null);
            upCount = (int) limits.stream().filter(limit -> !limit.getDate().isBefore(startDate) && !limit.getDate().isAfter(endDate)).count();
            brokenCount = limitCount - upCount;
        }
        return Pair.of(upCount, brokenCount);
    }

    private float getOpenPct(Daily daily) {
        return (daily.getOpen() - daily.getLastClose()) * 100/daily.getLastClose();
    }

    private float getIntradayPct(Daily daily) {
        return (daily.getClose() - daily.getOpen()) * 100/daily.getLastClose();
    }

    private float getAmpPct(Daily daily) {
        return (daily.getHigh() - daily.getLow()) * 100/daily.getLastClose();
    }

    private float getUpperShadow(Daily daily) {
        return (daily.getHigh() - daily.getClose())/daily.getLastClose();
    }

    private float getLowerShadow(Daily daily) {
        return (daily.getClose() - daily.getLow())/daily.getLastClose();
    }
}
