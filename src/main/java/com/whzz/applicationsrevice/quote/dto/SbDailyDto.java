package com.whzz.applicationsrevice.quote.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SbDailyDto {
    private String code;
    private LocalDate date;
    private float open;
    private float high;
    private float low;
    private float close;
    private float lastClose;
    private long volume;
    private long amount;
    private float turn;
    private float percent;
}
