package com.whzz.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "daily")
public class Daily {
    @Id
    private long id;
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
    private float limitUp;
    private float limitDown;
}
