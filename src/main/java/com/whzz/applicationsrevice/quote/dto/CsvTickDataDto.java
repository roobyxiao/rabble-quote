package com.whzz.applicationsrevice.quote.dto;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

@Data
public class CsvTickDataDto {
    private String time;
    private double price;
    @Alias("vol")
    private long volume;
    @Alias("buyorsell")
    private String bs;
}
