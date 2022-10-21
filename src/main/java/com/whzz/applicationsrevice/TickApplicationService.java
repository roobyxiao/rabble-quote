package com.whzz.applicationsrevice;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import com.whzz.domain.bo.Tick;
import com.whzz.domain.service.TickDomainService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class TickApplicationService {
    private final TickDomainService domainService;
    private String path = System.getProperty("user.dir");

    public void save(Tick tick) {
        domainService.save(tick);
    }

    public void saveToFile(Tick tick) {
        String date = tick.getDate();
        String[] dates = date.toString().split("-");
        String dir = new File(path).getParent() + "/data";
        File yearPath = new File(dir + "/" + dates[0]);
        File monthPath = new File(dir + "/" + dates[0] + "/" + dates[1]);
        File dayPath = new File(dir + "/" + dates[0] + "/" + dates[1] + "/" + dates[2]);
        if (!yearPath.exists() && !yearPath.isDirectory())
            yearPath.mkdir();
        if (!monthPath.exists() && !monthPath.isDirectory())
            monthPath.mkdir();
        if (!dayPath.exists() && !dayPath.isDirectory())
            dayPath.mkdir();
        File file = new File(dayPath.getAbsolutePath() + "/" + tick.getCode() + ".csv");
        CsvWriter writer =  CsvUtil.getWriter(file, CharsetUtil.CHARSET_ISO_8859_1);
        writer.writeBeans(tick.getData());
        writer.close();
    }
}
