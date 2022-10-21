package com.whzz.applicationsrevice.quote.dto;

import lombok.Data;

@Data
public class TsLimitDto {
    private String tsCode;
    private String tradeDate;
    private float upLimit;
    private float downLimit;
}
