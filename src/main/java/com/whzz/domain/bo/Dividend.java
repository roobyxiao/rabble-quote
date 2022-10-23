package com.whzz.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@IdClass(DividendId.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "dividend")
public class Dividend
{
    @Id
    private String code;
    @Id
    private LocalDate planDate;
    private LocalDate dividendDate;
    private BigDecimal ratio;
    private float bonus;
}
