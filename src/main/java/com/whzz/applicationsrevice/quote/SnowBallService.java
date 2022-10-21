package com.whzz.applicationsrevice.quote;

import com.alibaba.fastjson.JSON;
import com.whzz.applicationsrevice.quote.dto.SbDailyDto;
import com.whzz.applicationsrevice.quote.dto.SbStockDto;
import com.whzz.utils.OkHttpUtil;
import com.whzz.utils.SymbolUtil;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
@AllArgsConstructor
public class SnowBallService {

    private final OkHttpUtil okHttpUtil;

    public List<SbStockDto> getStocks () {
        var url = "https://xueqiu.com/service/v5/stock/preipo/cn/query?page=1&size=30&order=desc&order_by=list_date&type=quote";
        var response = JSON.parseObject(okHttpUtil.doGet(url));
        var data = response.getJSONObject("data");
        if (data != null) {
            var items = data.getString("items");
            return JSON.parseArray(items, SbStockDto.class);
        }
        return List.of();
    }

    public List<SbDailyDto> getDailiesByStock(String code, LocalDate startDate, int count, String type)
    {
        var url = "https://stock.xueqiu.com/v5/stock/chart/kline.json";
        var params = Map.of("symbol", SymbolUtil.codeToSbSymbol(code),
                "begin", startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() + "",
                "period", "day",
                "type", type,
                "count", count + "",
                "indicator", "kline,pe,pb,ps,pcf,market_capital,agt,ggt,balance");
        var response = JSON.parseObject(okHttpUtil.doGet(url, params));
        var data = response.getJSONObject("data");
        if (data != null) {
            var items = data.getJSONArray("item");
            if (items != null) {
                return items.stream().filter(item -> item != null).map(Object::toString).map(JSON::parseArray)
                        .map(item -> SbDailyDto.builder().code(code)
                                .date(LocalDateTime.ofInstant(Instant.ofEpochMilli(item.getLongValue(0)),
                                        ZoneId.systemDefault()).toLocalDate())
                                .volume(item.getLongValue(1))
                                .open(item.getFloatValue(2))
                                .high(item.getFloatValue(3))
                                .low(item.getFloatValue(4))
                                .close(item.getFloatValue(5))
                                .lastClose(item.getFloatValue(5) - item.getFloatValue(6))
                                .percent(item.getFloatValue(7))
                                .turn(item.getFloatValue(8))
                                .amount(item.getLongValue(9)).build()).collect(Collectors.toList());
            }
        }
        return List.of();
    }

    @Before(value = "@annotation(com.whzz.annotation.SnowBallCookie)")
    public void retrieveSbCookie(JoinPoint jp) {
        String xqUrl = "https://xueqiu.com/service/v5/stock/preipo/cn/query?page=1&size=30&order=desc&order_by=list_date&type=quote";
        okHttpUtil.doGet(xqUrl);
    }
}
