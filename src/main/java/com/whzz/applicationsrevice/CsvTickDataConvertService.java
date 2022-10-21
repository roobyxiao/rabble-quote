package com.whzz.applicationsrevice;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.CharsetUtil;
import com.whzz.applicationsrevice.quote.dto.CsvTickDataDto;
import com.whzz.domain.bo.Tick;
import com.whzz.utils.SymbolUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CsvTickDataConvertService {

    private final TickApplicationService tickApplicationService;
    private final DailyApplicationService dailyApplicationService;
    public final String pathDir = "/Users/C5275334/project/tick/2020";

    //沪深主板
    private Predicate<String> hsZb = s -> s.startsWith("00") || s.startsWith("60");

    @SneakyThrows
    public void convertAllCSV(){
        log.info("Processing tick");
        Files.walk(Paths.get(pathDir)).filter(Files::isRegularFile).
                forEach(path -> {
                    var fileName = path.getFileName().toString();
                    if (hsZb.test(fileName)) {
                        var dateStr = path.getParent().getFileName().toString();
                        System.out.println(path);
                        var symbol = fileName.split("\\.")[0];
                        var code = SymbolUtil.symbolToCode(symbol);
                        var date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
                        var daily = dailyApplicationService.findByCodeAndDate(code, date);
                        daily.ifPresent(x -> {
                            if (x.getHigh() / x.getLastClose() > 1.07) {
                                var csvReader = CsvUtil.getReader();
                                var rows = csvReader.read(ResourceUtil.getReader(path.toString(), CharsetUtil.CHARSET_GBK), CsvTickDataDto.class);
                                var datas = rows.stream().map(row -> {
                                    String buyorsell = row.getBs();
                                    int bs;
                                    switch (buyorsell) {
                                        case "2":
                                            bs = 4;
                                            break;
                                        case "0":
                                            bs = 1;
                                            break;
                                        case "1":
                                            bs = 2;
                                            break;
                                        default:
                                            bs = 1;
                                            break;
                                    }
                                    return Tick.TickData.builder().t(row.getTime())
                                            .p(row.getPrice())
                                            .v(row.getVolume())
                                            .bs(bs).build();
                                }).collect(Collectors.toList());
                                var tick = Tick.builder().code(code).date(date.toString()).data(datas).build();
                                tickApplicationService.save(tick);
                            }
                        });
                    }
                });
        log.info("Processed tick");
    }
}
