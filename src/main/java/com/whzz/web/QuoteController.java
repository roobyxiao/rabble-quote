package com.whzz.web;

import com.whzz.annotation.SnowBallCookie;
import com.whzz.applicationsrevice.TradeCalApplicationService;
import com.whzz.applicationsrevice.factor.FactorService;
import com.whzz.applicationsrevice.quote.QuoteService;
import com.whzz.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping(value = "/quote")
@AllArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;
    private final FactorService factorService;
    private final TradeCalApplicationService tradeCalApplicationService;
    @RequestMapping(value = "/daily")
    @SnowBallCookie
    public void updateDaily()
    {
        var date = LocalDate.now();
        quoteService.restoreCalendar(date.minusDays(20));
        if (tradeCalApplicationService.isOpen(date)) {
            quoteService.restoreStock(Constants.START_DATE);
            var codes = quoteService.restoreDividendByDate(date);
            codes.forEach(code -> factorService.reCalculateForwardFactor(code, Constants.START_DATE));
            var count1 = quoteService.restoreEmDailyByDate(date);
            quoteService.restoreEastMoneyLimit(date);
            factorService.calculateFactors(date);
            var count2 = quoteService.restoreTickByDate(date);
            assert count1 == count2;
        }
    }
    @RequestMapping(value = "/all")
    @SnowBallCookie
    public void updateAll()
    {
//        quoteService.restoreCalendar(Constants.WC_LIMIT_START_DATE);
//        quoteService.restoreStock(Constants.START_DATE);
//        quoteService.restoreAllSnowBallDailies(Constants.START_DATE, true);
//        quoteService.restoreDividends();
        //        quoteService.restoreTushareLimit();
//        quoteService.restoreWenCaiLimit();
        quoteService.restoreEastMoneyLimit(Constants.EM_LIMIT_START_DATE);
//        var date = LocalDate.now();
//        quoteService.restoreTickByDate(date);
    }

    @RequestMapping(value = "/custom")
    @SnowBallCookie
    public void updateCustom()
    {
        var date = LocalDate.now();
        quoteService.restoreTickByDate(date);
    }
}
