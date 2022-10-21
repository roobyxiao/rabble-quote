package com.whzz.applicationsrevice.quote;

import com.alibaba.fastjson.JSON;
import com.whzz.applicationsrevice.quote.dto.BsStockDto;
import com.whzz.applicationsrevice.quote.dto.BsTradeCalDto;
import com.whzz.applicationsrevice.quote.dto.TsLimitDto;
import com.whzz.utils.OkHttpUtil;
import com.whzz.utils.SymbolUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BaseUrlService {
    private final OkHttpUtil okHttpUtil;

    @Value("${base-url}")
    private String baseUrl;

    public List<BsTradeCalDto> getTradeCalsByDate(LocalDate startDate) {
        var url = baseUrl + "calendar";
        var endDate = LocalDate.now().plusMonths(1);
        var params = Map.of("start_date", startDate.toString(),
                "end_date", endDate.toString());
        var response = JSON.parseObject(okHttpUtil.doGet(url, params));
        if (response != null) {
            var error_code = response.getIntValue("error_code");
            if (error_code == 0) {
                var data = response.getString("data");
                return JSON.parseArray(data, BsTradeCalDto.class);
            }
        }
        return List.of();
    }

    public List<BsStockDto> getStocks() {
        var url = baseUrl + "stock";
        var response = JSON.parseObject(okHttpUtil.doGet(url));
        if (response != null){
            var error_code = response.getIntValue("error_code");
            if (error_code == 0) {
                var data = response.getString("data");
                return JSON.parseArray(data, BsStockDto.class);
            }
        }
        return List.of();
    }

    public List<String> getTushareCodes()
    {
        var url = baseUrl + "ts_stock";
        var codes = new ArrayList<String>();
        var response = JSON.parseObject(okHttpUtil.doGet(url));
        if (response != null) {
            var error_code = response.getIntValue("error_code");
            if (error_code == 0) {
                var datas = response.getJSONArray("data");
                for (int i = 0; i < datas.size(); i++) {
                    var data = datas.getJSONObject(i);
                    var market = data.getString("market");
                    if (!market.equals("主板"))
                        continue;
                    var tsCode = data.getString("ts_code").toLowerCase();
                    var code = SymbolUtil.tsCodeToCode(tsCode);
                    codes.add(code);
                }
            }
        }
        return codes;
    }

    public List<TsLimitDto> getTushareLimits(LocalDate date)
    {
        var url = baseUrl + "limit";
        var params = Map.of("trade_date", date.toString());
        var response = JSON.parseObject(okHttpUtil.doGet(url, params));
        if (response != null) {
            var error_code = response.getIntValue("error_code");
            if (error_code == 0) {
                var data = response.getString("data");
                return JSON.parseArray(data, TsLimitDto.class);
            }
        }
        return null;
    }

    public List<TsLimitDto> getTushareLimits(String code, LocalDate startDate, LocalDate endDate)
    {
        var url = baseUrl + "limit";
        var params = Map.of("ts_code", SymbolUtil.codeToTsCode(code),
                "start_date", startDate.toString(),
                "end_date", endDate.toString());
        var response = JSON.parseObject(okHttpUtil.doGet(url, params));
        if (response != null) {
            int error_code = response.getIntValue("error_code");
            if (error_code == 0) {
                var data = response.getString("data");
                return JSON.parseArray(data, TsLimitDto.class);
            }
        }
        return null;
    }
}
