package com.whzz.applicationsrevice.quote.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BsStockDto {
    private String code;
    private String codeName;
    private LocalDate ipoDate;
    private LocalDate outDate;
    private int type;
    private Boolean status;
}
