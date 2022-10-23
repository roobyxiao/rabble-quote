package com.whzz.web;

import com.whzz.annotation.SnowBallCookie;
import com.whzz.applicationsrevice.TradeCalApplicationService;
import com.whzz.applicationsrevice.quote.QuoteService;
import com.whzz.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/quote")
@AllArgsConstructor
public class QuoteController {
    private final QuoteService quoteService;

    @RequestMapping(value = "/all")
    @SnowBallCookie
    public void updateAll()
    {
        //quoteService.restoreCalendar(Constants.WC_LIMIT_START_DATE);
        //quoteService.restoreStock(Constants.START_DATE);
        //quoteService.restoreAllSnowBallDailies(Constants.START_DATE, false);
        //quoteService.restoreDividends();
        quoteService.restoreTushareLimit();
        quoteService.restoreWenCaiLimit();
        quoteService.restoreEastMoneyLimit(Constants.EM_LIMIT_START_DATE);
    }
}
