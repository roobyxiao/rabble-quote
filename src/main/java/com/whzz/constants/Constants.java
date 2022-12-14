package com.whzz.constants;

import java.time.LocalDate;
import java.util.List;

public class Constants {

    public static final LocalDate START_DATE = LocalDate.parse("2020-01-01");

    public static final LocalDate EM_LIMIT_START_DATE = LocalDate.parse("2019-12-02");

    public static final LocalDate WC_LIMIT_START_DATE = LocalDate.parse("2014-06-26");

    public static final LocalDate WC_LIMIT_END_DATE = LocalDate.parse("2019-12-01");

    public static final String SNOW_BALL_DAILY_TYPE_BEFORE = "before";

    public static final String SNOW_BALL_DAILY_TYPE_NORMAL = "normal";

    public static final List<String> SKIP_CODES = List.of("sh.603800", "sz.301068");
}
