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
@Table(name = "limit_factor")
public class LimitFactor {
    @Id
    private String code;
    @Id
    private LocalDate date;
    private float auctionVolume;
}
