package com.whzz.applicationsrevice.quote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzz.domain.bo.UpLimit;
import com.whzz.utils.OkHttpUtil;
import com.whzz.utils.SymbolUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class WenCaiService {

    private final OkHttpUtil okHttpUtil;

    public List<UpLimit> getWenCaiLimits(LocalDate date)
    {
        var url = "https://m.iwencai.com/unified-wap/get-parser-data?w="+date.toString()+"%E6%B6%A8%E5%81%9C%E8%82%A1%E7%A5%A8";
        var limits = new ArrayList<UpLimit>();
        var count = 0;
        var params = new HashMap();
        params.put("condition", "[{\"indexName\":\"涨停\",\"indexProperties\":[\"交易日期 "
                + date.format(DateTimeFormatter.BASIC_ISO_DATE)
                + "\"],\"indexPropertiesMap\":{\"交易日期\":\""
                + date.format(DateTimeFormatter.BASIC_ISO_DATE)
                +"\"},\"type\":\"index\",\"sonSize\":0,\"reportType\":\"TRADE_DAILY\",\"valueType\":\"_是否\",\"source\":\"new_parser\",\"dateType\":\"交易日期\",\"dateUnit\":\"日\",\"tag\":\"["
                + date.toString()
                + "]涨停\",\"dateText\":\""
                + date.toString()
                + "\",\"uiText\":\""+ date.getYear() + "年" + date.getMonthValue() + "月" + date.getDayOfMonth() + "日的涨停\","
                + "\"chunkedResult\":\"" + date.toString() +"涨停股票\","
                + "\"queryText\":\""+ date.getYear() + "年" + date.getMonthValue() + "月" + date.getDayOfMonth() + "日的涨停\",\"relatedSize\":0}]");
        var response = JSON.parseObject(okHttpUtil.doPost(url, params));
        var result = response.getJSONObject("data");
        if (result != null) {
            var token = result.getString("token");
            var items = result.getJSONArray("data");
            if (items != null) {
                count += items.size();
                for (int i = 0; i < items.size(); i++) {
                    var item = items.getJSONObject(i);
                    limits.add(convertWenCaiLimit(item, date));
                }
            }
            if (items != null && items.size() == 30) {
                var retrieveNext = true;
                var page = 2;
                while (retrieveNext) {
                    var nextUrl = "http://m.iwencai.com/unified-wap/cache?page="+page+"&perpage=30&token="+token;
                    var nextResponse = JSON.parseObject(okHttpUtil.doGet(nextUrl));
                    var nextResult = nextResponse.getJSONObject("data");
                    if (nextResult != null) {
                        var nextItems = nextResult.getJSONArray("data");
                        if (nextItems != null) {
                            count += nextItems.size();
                            page++;
                            if (nextItems.size() != 30)
                                retrieveNext = false;
                            for (int i = 0; i < nextItems.size(); i++) {
                                var item = nextItems.getJSONObject(i);
                                limits.add(convertWenCaiLimit(item, date));
                            }
                        } else {
                            retrieveNext = false;
                        }
                    }
                }
            }
        }
        log.warn(date + "涨停板信息同步" + count + "条");
        return limits;
    }

    private UpLimit convertWenCaiLimit(JSONObject item, LocalDate date)
    {
        var tsCode = item.getString("股票代码");
        var code = SymbolUtil.tsCodeToCode(tsCode);
        var firstTimeStr = item.getString("首次涨停时间[" + date.format(DateTimeFormatter.BASIC_ISO_DATE) + "]");
        var endTimeStr = item.getString("最终涨停时间[" + date.format(DateTimeFormatter.BASIC_ISO_DATE) + "]");
        var open = item.getIntValue("涨停开板次数[" + date.format(DateTimeFormatter.BASIC_ISO_DATE) + "]");
        var last = item.getIntValue("连续涨停天数[" + date.format(DateTimeFormatter.BASIC_ISO_DATE) + "]");
        UpLimit limit = UpLimit.builder().code(code).date(date)
                .firstTime(firstTimeStr == null ? null : LocalTime.parse(firstTimeStr))
                .endTime(endTimeStr == null ? null : LocalTime.parse(endTimeStr))
                .open(open).last(last).build();
        return limit;
    }

}
