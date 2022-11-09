package com.whzz.applicationsrevice.quote;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Streams;
import com.google.common.util.concurrent.RateLimiter;
import com.whzz.applicationsrevice.*;
import com.whzz.constants.Constants;
import com.whzz.domain.bo.*;
import com.whzz.utils.SymbolUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuoteService {
    private final ModelMapper modelMapper;
    private final BaseUrlService baseUrlService;
    private final SnowBallService snowBallService;
    private final EastMoneyService eastMoneyService;
    private final WenCaiService wenCaiService;
    private final TradeCalApplicationService tradeCalApplicationService;
    private final StockApplicationService stockApplicationService;
    private final DailyApplicationService dailyApplicationService;
    private final ForwardApplicationService forwardApplicationService;
    private final UpLimitApplicationService upLimitApplicationService;
    private final DividendApplicationService dividendApplicationService;
    private final TickApplicationService tickApplicationService;

    private Predicate<Dividend> unusedDividend = dividend -> dividend.getRatio() == null
            || dividend.getRatio().compareTo(BigDecimal.ZERO) == 0;
    /**
     * 交易日查询
     * baostock query_trade_dates()
     * @param startDate
     */
    public void restoreCalendar(LocalDate startDate)
    {
        log.warn("开始同步交易日历······");
        var dtos = baseUrlService.getTradeCalsByDate(startDate);
        var cals = dtos.stream().map(dto -> modelMapper.map(dto, TradeCal.class)).collect(Collectors.toList());
        tradeCalApplicationService.saveTradeCals(cals);
        log.warn("交易日历同步" + cals.size() + "条······");
    }

    /**
     * 证券基本资料
     * baostock query_stock_basic()
     * baostock缺失数据从雪球 新股行情 获取
     */
    @Transactional
    public void restoreStock(LocalDate startDate)
    {
        log.warn("开始同步证券基本资料······");
        var bsDtos = baseUrlService.getStocks();
        var stocks = bsDtos.stream().filter(dto -> dto.getType() == 1 && SymbolUtil.isHs(dto.getCode())
                        && (dto.getStatus() || dto.getOutDate().isAfter(startDate)))
                .map(dto -> modelMapper.map(dto, Stock.class)).collect(Collectors.toList());
        stockApplicationService.saveAll(stocks);
        log.warn("证券基本资料同步baostock" + stocks.size() + "条······");
        var sbDtos = snowBallService.getStocks();
        stocks = sbDtos.stream().filter(dto -> !stockApplicationService.stockExists(SymbolUtil.sbSymbolToCode(dto.getSymbol()))
                        && SymbolUtil.isHs(SymbolUtil.sbSymbolToCode(dto.getSymbol())))
                .map(dto -> modelMapper.map(dto, Stock.class)).collect(Collectors.toList());
        stocks.forEach(stock -> log.warn(stock.getCode()));
        stockApplicationService.saveAll(stocks);
        log.warn("证券基本资料同步雪球" + stocks.size() + "条······");
        var emDtos = eastMoneyService.getStocks();
        stocks = emDtos.stream().filter(dto -> dto.getMARKETTYPE().equals("非科创板")
                        && !stockApplicationService.stockExists(SymbolUtil.symbolToCode(dto.getSECURITYCODE())))
                .map(dto -> modelMapper.map(dto, Stock.class)).collect(Collectors.toList());
        stockApplicationService.saveAll(stocks);
        log.warn("证券基本资料同步东方财富网" + stocks.size() + "条······");
        var codes =baseUrlService.getTushareCodes();
        codes.forEach(code -> {
            if (!stockApplicationService.stockExists(code))
                log.error("证券基本资料校验" + code + "不存在······");
        });
        log.warn("证券基本资料同步完成······");
    }

    /**
     * A股K线数据
     * 雪球
     * @param startDate
     */
    public void restoreAllSnowBallDailies(LocalDate startDate, boolean containToday)
    {
        log.warn("开始同步日线······");
        var stocks = stockApplicationService.findAll();
        stocks.forEach(stock -> {
            restoreSnowBallDailiesByCode(stock.getCode(), startDate, containToday);
        });
        log.warn("日线同步完成······");
    }

    public void restoreSnowBallDailiesByCode(String code, LocalDate startDate, boolean containToday) {
        //不复权
        var beforeDailyStream = snowBallService.getDailiesByStock(code, startDate, -250, Constants.SNOW_BALL_DAILY_TYPE_NORMAL).stream();
        var afterDailyStream = snowBallService.getDailiesByStock(code, startDate, 3000, Constants.SNOW_BALL_DAILY_TYPE_NORMAL).stream();
        var dailies = Stream.concat(beforeDailyStream, afterDailyStream)
                .map(daily -> modelMapper.map(daily, Daily.class)).collect(Collectors.toList());
        if (!containToday)
            dailies = dailies.stream().filter(daily -> daily.getDate().isBefore(LocalDate.now())).collect(Collectors.toList());
        dailyApplicationService.insertAll(dailies);
        //前复权
        var beforeForwardStream = snowBallService.getDailiesByStock(code, startDate, -250, Constants.SNOW_BALL_DAILY_TYPE_BEFORE).stream();
        var afterForwardStream = snowBallService.getDailiesByStock(code, startDate, 3000, Constants.SNOW_BALL_DAILY_TYPE_BEFORE).stream();
        var forwards = Stream.concat(beforeForwardStream, afterForwardStream)
                .map(forward -> modelMapper.map(forward, Forward.class)).collect(Collectors.toList());
        if (!containToday)
            forwards = forwards.stream().filter(forward -> forward.getDate().isBefore(LocalDate.now())).collect(Collectors.toList());
        forwardApplicationService.insertAll(forwards);
        if (forwards.size() != dailies.size())
            log.error(code + "复权数据不匹配");
        log.warn(code + "日线同步" + dailies.size() + "条······");
    }


    /**
     * A股当日K线
     * 东方财富网
     * @param date
     */
    public int restoreEmDailyByDate(LocalDate date)
    {
        log.warn("开始同步当日K线······");
        var dtos = eastMoneyService.getEmDailiesByDate(date);
        var dailies = dtos.stream().map(dto -> modelMapper.map(dto, Daily.class)).collect(Collectors.toList());
        var forwards = dtos.stream().map(dto -> modelMapper.map(dto, Forward.class)).collect(Collectors.toList());
        dailyApplicationService.insertAll(dailies);
        forwardApplicationService.insertAll(forwards);
        log.warn("当日K线同步完成······");
        return dtos.size();
    }

    /**
     * 每日涨跌停价格
     * tushare
     */
    public void restoreTushareLimit()
    {
        log.warn("开始同步每日涨跌停价格······");
        var stocks = stockApplicationService.findAll();
        var limiter = RateLimiter.create(0.8);
        stocks.forEach(stock -> {
            var dailies = dailyApplicationService.findByCode(stock.getCode());
            limiter.acquire();
            var startDate = dailies.stream().findFirst().map(Daily::getDate).orElse(null);
            var endDate = Streams.findLast(dailies.stream()).map(Daily::getDate).orElse(null);
            if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
                var dtos = baseUrlService.getTushareLimits(stock.getCode(), startDate, endDate);
                var updateDailies = dtos.stream().map(dto -> modelMapper.map(dto, Daily.class)).collect(Collectors.toList());
                dailyApplicationService.updateLimits(updateDailies);
                log.warn(stock.getCode() + "-每日涨跌停价格同步完成······");
            }
        });
        log.warn("每日涨跌停价格同步完成······");
    }

    /**
     * 涨停板行情
     * 东方财富网
     * @param startDate
     */
    public void restoreEastMoneyLimit(LocalDate startDate)
    {
        log.warn("开始同步涨停板信息······");
        var tradeCals = tradeCalApplicationService.findOpenCals(startDate);
        tradeCals.forEach(cal -> {
            var dtos = eastMoneyService.getEastMoneyLimits(cal.getDate());
            var limits = dtos.stream().filter(dto -> SymbolUtil.isHs(SymbolUtil.symbolToCode(dto.getC())))
                    .map(dto -> modelMapper.map(dto, UpLimit.class))
                    .map(limit -> {
                        var daily = dailyApplicationService.findById(limit.getCode(), cal.getDate());
                        return daily.map(x -> {
                            limit.setDate(cal.getDate());
                            limit.setKeep(x.getLow() == x.getHigh() ? true : false);
                            return limit;
                        }).orElse(null);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            upLimitApplicationService.saveAll(limits);
            log.warn(cal.getDate() + "涨停板信息同步完成······");
        });
        log.warn("涨停板信息同步完成······");
    }

    /**
     * 涨停板行情
     * 同花顺问财
     */
    public void restoreWenCaiLimit()
    {
        log.warn("开始同步涨停板信息······");
        var tradeCals = tradeCalApplicationService.findOpenCals(Constants.WC_LIMIT_START_DATE, Constants.WC_LIMIT_END_DATE);
        tradeCals.forEach(tradeCal -> {
            var limits = wenCaiService.getWenCaiLimits(tradeCal.getDate());
            var results = limits.stream().filter(limit -> SymbolUtil.isHs(limit.getCode()))
                    .map(limit -> {
                        var daily = dailyApplicationService.findById(limit.getCode(), limit.getDate());
                        return daily.filter(x -> x.getPercent() > 7).map(x -> {
                            limit.setKeep(x.getLow() == x.getHigh() ? true : false);
                            return limit;
                        }).orElse(null);
                    }).filter(Objects::nonNull)
                    .collect(Collectors.toList());
            upLimitApplicationService.saveAll(results);
        });
        log.warn("涨停板信息同步完成······");
    }

    public void restoreDividends()
    {
        log.warn("开始同步分红送配······");
        var stocks = stockApplicationService.findAll();
        stocks.forEach(stock -> {
            var dividends = eastMoneyService.getDividendsByCode(stock.getCode()).stream()
                    .filter(emDividendDto -> StringUtils.isNotBlank(emDividendDto.getPLANNOTICEDATE())
                            && StringUtils.isNotBlank(emDividendDto.getEXDIVIDENDDATE()))
                    .map(x -> modelMapper.map(x, Dividend.class)).collect(Collectors.toList());
            var forwards = forwardApplicationService.findByCodeOrderByDateDesc(stock.getCode());
            if (!dividends.isEmpty() && !forwards.isEmpty()) {
                var startDate = Streams.findLast(forwards.stream()).map(Forward::getDate);
                startDate.ifPresent(x -> {
                    dividends.removeIf(dividend -> !dividend.getDividendDate().isAfter(x) || dividend.getDividendDate().isAfter(LocalDate.now()));
                    dividendApplicationService.saveAll(dividends);
                    dividends.removeIf(unusedDividend);
                    if (!dividends.isEmpty())
                        updateForwardsByDividends(forwards, dividends);
                    log.warn(stock.getCode() + "分红送配······");
                });
            }
        });
        log.warn("分红送配同步完成······");
    }

    /**
     * 分红送配 当日数据
     * 东方财富网
     */
    public List<String> restoreDividendByDate(LocalDate date) {
        log.warn("开始同步当日分红送配······");
        var emDividendDtos= eastMoneyService.getDividendsByDate(date);
        var dividendList = emDividendDtos.stream()
                .map(emDividendDto -> modelMapper.map(emDividendDto, Dividend.class))
                .filter(dividend -> dividend.getDividendDate().equals(LocalDate.now()))
                .collect(Collectors.toList());
        var codes = dividendList.stream().map(dividend -> {
            var dividends = dividendApplicationService.findByCode(dividend.getCode());
            var forwardList = forwardApplicationService.findByCodeOrderByDateDesc(dividend.getCode());
            dividends.add(0, dividend);
            dividends.removeIf(unusedDividend);
            forwardApplicationService.deleteAll(forwardList);
            var forwards = restoreForwardsByStock(dividend.getCode(), Constants.START_DATE, false);
            forwardApplicationService.insertAll(forwards);
            if (!dividends.isEmpty()) {
                updateForwardsByDividends(forwards, dividends);
            }
            log.warn("处理" + dividend.getCode() + "当日分红送配······");
            return dividend.getCode();
        }).collect(Collectors.toList());
        dividendApplicationService.saveAll(dividendList);
        log.warn("当日分红送配同步完成······");
        return codes;
    }

    /**
     * tick数据
     * 东方财富网
     * @param date
     */
    public int restoreTickByDate(LocalDate date)
    {
        log.warn("开始同步tick数据······");
        int count = 0;
        var stocks = stockApplicationService.getListedStocks();
        for (Stock stock: stocks){
            var code = stock.getCode();
            var tickDataDtos = eastMoneyService.getTickByDate(code, date);
            if (!tickDataDtos.isEmpty()) {
                count++;
                var tickData = tickDataDtos.stream().map(tickDataDto -> modelMapper.map(tickDataDto, Tick.TickData.class)).collect(Collectors.toList());
                var tick = Tick.builder().code(code).date(date.toString()).data(tickData).build();
                tickApplicationService.saveToFile(tick);
            }
        }
        log.warn("tick数据同步完成······");
        return count;
    }

    /**
     * A股K线数据 (前复权)
     * 雪球
     * @param startDate
     */
    private List<Forward> restoreForwardsByStock(String code, LocalDate startDate, boolean containToday)
    {
        var beforeForwardStream = snowBallService.getDailiesByStock(code, startDate, -250, Constants.SNOW_BALL_DAILY_TYPE_BEFORE).stream();
        var afterForwardStream = snowBallService.getDailiesByStock(code, startDate, 3000, Constants.SNOW_BALL_DAILY_TYPE_BEFORE).stream();
        var forwards = Stream.concat(beforeForwardStream, afterForwardStream);
        if (!containToday)
            forwards = forwards.filter(forward -> forward.getDate().isBefore(LocalDate.now()));
        return forwards.map(forward -> modelMapper.map(forward, Forward.class)).collect(Collectors.toList());
    }


    private void updateForwardsByDividends(List<Forward> forwards, List<Dividend> dividends) {
        var sortedForwards = forwards.stream().sorted(Comparator.comparing(Forward::getDate).reversed()).collect(Collectors.toList());
        var sortedDividends = dividends.stream().sorted(Comparator.comparing(Dividend::getDividendDate).reversed()).collect(Collectors.toList());
        sortedForwards.stream().forEach(forward -> {
            var ratio = sortedDividends.stream()
                    .filter(dividend -> forward.getDate().isBefore(dividend.getDividendDate()))
                    .map(Dividend::getRatio)
                    .map(BigDecimal::floatValue)
                    .map(x -> (x + 10)/10)
                    .reduce(1f, (a, b) -> a * b);
            forward.setRatio(ratio);
        });
        forwardApplicationService.updateAll(forwards);
    }

}
