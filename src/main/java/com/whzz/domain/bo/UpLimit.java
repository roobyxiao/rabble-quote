package com.whzz.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "up_limit")
public class UpLimit {
    private long id;
    private String code;
    private LocalDate date;
    //首次涨停时间
    private LocalTime firstTime;
    //最终涨停时间
    private LocalTime endTime;
    //开板次数
    private int open;
    //连板次数
    private int last;
    //是否一字涨停
    @Builder.Default
    private boolean keep = false;
    @Transient
    @Builder.Default
    private boolean persist = false;
}
