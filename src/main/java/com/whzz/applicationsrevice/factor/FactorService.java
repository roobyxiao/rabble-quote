package com.whzz.applicationsrevice.factor;

import com.whzz.applicationsrevice.StockApplicationService;
import com.whzz.constants.Constants;
import com.whzz.domain.bo.Stock;
import com.whzz.utils.SymbolUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@AllArgsConstructor
public class FactorService {
    private StockApplicationService stockApplicationService;
    private ForwardFactorApplicationService forwardFactorApplicationService;
    private DailyFactorApplicationService dailyFactorApplicationService;

    public void calculateFactors(LocalDate date) {
        calculateDailyFactors(date);
        calculateForwardFactors(date);
    }

    public void calculateForwardFactors(LocalDate startDate) {
        log.warn("Processing forward factor······");
        var stocks = stockApplicationService.findAll();
        stocks.stream()
                .map(Stock::getCode)
                .filter(SymbolUtil::isHsZB)
                .filter(code -> !Constants.SKIP_CODES.contains(code))
                .forEach(code -> forwardFactorApplicationService.calculateForwardFactorByCode(code, startDate));
        log.warn("Processed forward factor······");
    }

    public void reCalculateForwardFactor(String code, LocalDate startDate) {
        forwardFactorApplicationService.reCalculateForwardFactor(code, startDate);
    }

    public void calculateDailyFactors(LocalDate startDate) {
        log.warn("Processing daily factor······");
        var stocks = stockApplicationService.findAll();
        stocks.stream()
                .map(Stock::getCode)
                .filter(SymbolUtil::isHsZB)
                .filter(code -> !Constants.SKIP_CODES.contains(code))
                .forEach(code -> dailyFactorApplicationService.calculateDailyFactorByCode(code, startDate));
        log.info("Processed daily factor");
    }
}
