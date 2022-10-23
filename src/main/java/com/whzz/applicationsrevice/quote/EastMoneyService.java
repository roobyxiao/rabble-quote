package com.whzz.applicationsrevice.quote;

import com.alibaba.fastjson.JSON;
import com.whzz.applicationsrevice.StockApplicationService;
import com.whzz.applicationsrevice.quote.dto.*;
import com.whzz.domain.bo.Dividend;
import com.whzz.domain.bo.Stock;
import com.whzz.utils.OkHttpUtil;
import com.whzz.utils.SymbolUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EastMoneyService {
    private final OkHttpUtil okHttpUtil;
    private final StockApplicationService stockApplicationService;

    public List<EmStockDto> getStocks() {
        var url = "https://datacenter-web.eastmoney.com/api/data/v1/get?sortColumns=LISTING_DATE&sortTypes=-1&pageSize=100&pageNumber=1&reportName=RPTA_APP_IPOAPPLY&columns=SECURITY_CODE%2CSECURITY_NAME%2CTRADE_MARKET_CODE%2CAPPLY_CODE%2CTRADE_MARKET%2CMARKET_TYPE%2CORG_TYPE%2CISSUE_NUM%2CONLINE_ISSUE_NUM%2COFFLINE_PLACING_NUM%2CTOP_APPLY_MARKETCAP%2CPREDICT_ONFUND_UPPER%2CONLINE_APPLY_UPPER%2CPREDICT_ONAPPLY_UPPER%2CISSUE_PRICE%2CLATELY_PRICE%2CCLOSE_PRICE%2CAPPLY_DATE%2CBALLOT_NUM_DATE%2CBALLOT_PAY_DATE%2CLISTING_DATE%2CAFTER_ISSUE_PE%2CONLINE_ISSUE_LWR%2CINITIAL_MULTIPLE%2CINDUSTRY_PE_NEW%2COFFLINE_EP_OBJECT%2CCONTINUOUS_1WORD_NUM%2CTOTAL_CHANGE%2CPROFIT%2CLIMIT_UP_PRICE%2CINFO_CODE%2COPEN_PRICE%2CLD_OPEN_PREMIUM%2CLD_CLOSE_CHANGE%2CTURNOVERRATE%2CLD_HIGH_CHANG%2CLD_AVERAGE_PRICE%2COPEN_DATE%2COPEN_AVERAGE_PRICE%2CPREDICT_PE%2CPREDICT_ISSUE_PRICE2%2CPREDICT_ISSUE_PRICE%2CPREDICT_ISSUE_PRICE1%2CPREDICT_ISSUE_PE%2CPREDICT_PE_THREE%2CONLINE_APPLY_PRICE%2CMAIN_BUSINESS%2CPAGE_PREDICT_PRICE1%2CPAGE_PREDICT_PRICE2%2CPAGE_PREDICT_PRICE3%2CPAGE_PREDICT_PE1%2CPAGE_PREDICT_PE2%2CPAGE_PREDICT_PE3%2CSELECT_LISTING_DATE%2CIS_BEIJING&quoteColumns=f2~01~SECURITY_CODE~NEWEST_PRICE&filter=(APPLY_DATE%3E%272010-01-01%27)";
        var emResponse = JSON.parseObject(okHttpUtil.doGet(url));
        var result = emResponse.getJSONObject("result");
        if (result != null) {
            var data = result.getString("data");
            return JSON.parseArray(data, EmStockDto.class);
        }
        return List.of();
    }

    public List<EmDailyDto> getEmDailiesByDate(LocalDate date)
    {
        var url = "https://push2.eastmoney.com/api/qt/stock/get";
        var stocks = stockApplicationService.getListedStocks();
        return stocks.stream()
                .map(Stock::getCode)
                .filter(this::isCodeActive)
                .map(code -> {
                    var params = Map.of("fields", "f43,f44,f45,f46,f47,f48,f51,f52,f57,f60,f168,f170",
                            "fltt", "2",
                            "secid", SymbolUtil.getSecId(code));
                    var response = JSON.parseObject(okHttpUtil.doGet(url, params));
                    return response.getJSONObject("data");
                })
                .filter(Objects::nonNull)
                .map(data -> JSON.parseObject(data.toJSONString(), EmDailyDto.class))
                .filter(dailyDto -> dailyDto.getF46() != 0)
                .map(dailyDto -> {
                    dailyDto.setF57(SymbolUtil.symbolToCode(dailyDto.getF57()));
                    dailyDto.setDate(date);
                    return dailyDto;
                }).collect(Collectors.toList());
    }

    private boolean isCodeActive(String code) {
        var url = "https://push2his.eastmoney.com/api/qt/stock/trends2/get";
        var params = Map.of("fields1", "f4",
                "fields2", "f51",
                "secid", SymbolUtil.getSecId(code));
        var response = JSON.parseObject(okHttpUtil.doGet(url, params));
        //log.error(code);
        var data = response.getJSONObject("data");
        if (data != null) {
            var status = data.getIntValue("status");
            return status == 0;
        }
        return false;
    }

    public List<EmLimitDto> getEastMoneyLimits(LocalDate date)
    {
        var url = "https://push2ex.eastmoney.com/getTopicZTPool";
        var params = Map.of("ut", "7eea3edcaed734bea9cbfc24409ed989",
                "dpt", "wz.ztzt",
                "Pageindex", 0 + "",
                "pagesize", 300 + "",
                "sort", "fbt%3Aasc",
                "date", date.format(DateTimeFormatter.BASIC_ISO_DATE));
        var response = JSON.parseObject(okHttpUtil.doGet(url, params));
        var result = response.getJSONObject("data");
        if (result != null) {
            var data = result.getString("pool");
            return JSON.parseArray(data, EmLimitDto.class);
        }
        return List.of();
    }

    /**
     * 分红送配
     * 东方财富网
     */
    public List<EmDividendDto> getDividendsByCode(String code) {
        var symbol = SymbolUtil.codeToSymbol(code);
        var url = "https://datacenter-web.eastmoney.com/api/data/v1/get";
        var params = Map.of("sortColumns", "EX_DIVIDEND_DATE",
                "sortTypes", "-1",
                "reportName", "RPT_SHAREBONUS_DET",
                "columns", "ALL",
                "quoteColumns", "",
                "pageSize", "500",
                "filter", "(SECURITY_CODE%3D\""+symbol+"\")",
                "pageNumber", "1");
        var response = JSON.parseObject(okHttpUtil.doGet(url, params));
        var result = response.getJSONObject("result");
        if (result != null) {
            var data = result.getString("data");
            var dividends = JSON.parseArray(data, EmDividendDto.class);
            dividends.removeIf(dividend -> dividend.getEXDIVIDENDDATE() == null);
            return dividends;
        }
        return List.of();
    }


    /**
     * 分红送配 当日数据
     * 东方财富网
     */
    public List<Dividend> getDividendsByDate(LocalDate date) {
        var url = "https://datacenter-web.eastmoney.com/api/data/v1/get";
        var params = Map.of("sortColumns", "EX_DIVIDEND_DATE",
                "sortTypes", -1 + "",
                "reportName", "RPT_SHAREBONUS_DET",
                "columns", "ALL",
                "quoteColumns", "",
                "pageSize", 500 + "",
                "pageNumber", 1 + "");
        var response = JSON.parseObject(okHttpUtil.doGet(url, params));
        var result = response.getJSONObject("result");
        if (result != null) {
            var data = result.getString("data");
            var dividends = JSON.parseArray(data, Dividend.class);
            dividends.removeIf(dividend -> !SymbolUtil.isHs(dividend.getCode()) ||
                    !dividend.getDividendDate().isEqual(date));
            return dividends;
        }
        return List.of();
    }

    /**
     * tick数据
     * 东方财富网
     * @param date
     */
    public List<EmTickDataDto> getTickByDate(String code, LocalDate date)
    {
        var url = "https://push2ex.eastmoney.com/getStockFenShi";
        var codes = code.split("\\.");
        var pageSize = 1000;
        var dtos = new ArrayList();
        for (int i = 0 ; i <= 100 ; i++) {
            var params = Map.of("pagesize", pageSize + "",
                    "ut", "7eea3edcaed734bea9cbfc24409ed989",
                    "dpt", "wzfscj",
                    "sort", "1",
                    "ft", "1",
                    "code", codes[1],
                    "id", codes[1] + ("sh".equalsIgnoreCase(codes[0]) ? 1 : 2),
                    "market", ("sh".equalsIgnoreCase(codes[0]) ? "1" : "0"),
                    "pageindex", i + "");
            var response = JSON.parseObject(okHttpUtil.doGet(url, params));
            var result = response.getJSONObject("data");
            var data = result.getString("data");
            if (result != null) {
                var datas = JSON.parseArray(data, EmTickDataDto.class);
                dtos.addAll(datas);
                if (datas.size() < pageSize)
                    break;
            }
        }
        return dtos;
    }
}
