package com.whzz.utils;

import java.time.LocalTime;

public class DataTimeUtil {
    public static LocalTime emTimeToLocalTime(String value)
    {
        if (value.length() == 5)
            value = "0" + value;
        var time = value.replaceAll("(.{2})", ":$1").substring(1);
        return LocalTime.parse(time);
    }
}
