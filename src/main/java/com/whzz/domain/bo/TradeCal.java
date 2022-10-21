package com.whzz.domain.bo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "calendar")
public class TradeCal {
    @Id
    private LocalDate date;
    private boolean open;
    private LocalDate lastTradeDay;
}
