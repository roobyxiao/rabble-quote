package com.whzz.domain.bo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DividendId implements Serializable
{
    private String code;

    private LocalDate planDate;
}
