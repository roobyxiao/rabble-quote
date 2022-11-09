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
@Table(name = "daily_factor")
public class DailyFactor {
    @Id
    private String code;
    @Id
    private LocalDate date;
    //开盘 涨跌幅
    private float openPct;
    //日内 涨跌幅
    private float intradayPct;
    //振幅
    private float ampPct;
    //上影线
    private float upperShadow;
    //下影线
    private float lowerShadow;
    //20日涨停次数
    private int monthUp;
    //20日破板次数
    private int monthBroken;
    //60日涨停次数
    private int quarterUp;
    //60日破板次数
    private int quarterBroken;
    //250涨停次数
    private int yearUp;
    //250破板次数
    private int yearBroken;

}
