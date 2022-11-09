package com.whzz.domain.bo.factor;

import com.whzz.domain.bo.DailyId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@IdClass(DailyId.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "forward_factor")
public class ForwardFactor {
    @Id
    private String code;
    @Id
    private LocalDate date;
    //日均线
    private Float MA5;
    private Float MA10;
    private Float MA20;
    private Float MA59;
    private Float MA60;
    private Float MA249;
    private Float MA250;
    //日均量
    private Long VA5;
    private Long VA10;
    private Long VA20;
    private Long VA60;
    private Long VA250;
}
