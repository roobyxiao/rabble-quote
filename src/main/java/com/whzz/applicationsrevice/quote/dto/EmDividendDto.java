package com.whzz.applicationsrevice.quote.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmDividendDto {
    private String SECUCODE;
    private String PLANNOTICEDATE;
    private String EXDIVIDENDDATE;
    private BigDecimal BONUSITRATIO;
    private float PRETAXBONUSRMB;
}
