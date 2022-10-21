package com.whzz.domain.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "dividend")
public class Dividend
{
    @Id
    private long id;
    private String code;
    private LocalDate planDate;
    private LocalDate dividendDate;
    private BigDecimal ratio;
    private float bonus;
}
