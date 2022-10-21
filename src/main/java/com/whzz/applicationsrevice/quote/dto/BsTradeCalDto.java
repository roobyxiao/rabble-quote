package com.whzz.applicationsrevice.quote.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BsTradeCalDto {
    private LocalDate calendarDate;
    private Boolean isTradingDay;
}
