package com.whzz.applicationsrevice.quote.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmDailyDto {
    //symbol
    private String f57;
    private LocalDate date;
    //开盘价
    private float f46;
    //最高价
    private float f44;
    //最低价
    private float f45;
    //收盘价
    private float f43;
    //昨收价
    private float f60;
    //成交量
    private long f47;
    //成交额
    private long f48;
    //换手率
    private float f168;
    //涨跌幅
    private float f170;
    //涨停价
    private float f51;
    //跌停价
    private float f52;
}
