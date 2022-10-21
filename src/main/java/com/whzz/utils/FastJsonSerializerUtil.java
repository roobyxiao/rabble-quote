package com.whzz.utils;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import java.lang.reflect.Type;
import java.time.*;

public class FastJsonSerializerUtil
{
    public static class TSCodeFormat implements ObjectDeserializer
    {
        @Override
        public String deserialze (DefaultJSONParser defaultJSONParser,
                                 Type type,
                                 Object o)
        {
            String code = defaultJSONParser.getLexer().stringVal();
            String[] codes = code.split("\\.");
            code = codes[1].toLowerCase() + "." + codes[0];
            return code;
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }

    public static class XqSymbolFormat implements ObjectDeserializer
    {
        @Override
        public String deserialze (DefaultJSONParser defaultJSONParser,
                                  Type type,
                                  Object o)
        {
            String code = defaultJSONParser.getLexer().stringVal();
            code = code.substring(0,2).toLowerCase() + "." + code.substring(2);
            return code;
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }

    public static class CodeFormat implements ObjectDeserializer
    {
        @Override
        public String deserialze (DefaultJSONParser defaultJSONParser,
                                 Type type,
                                 Object o)
        {
            String code = defaultJSONParser.getLexer().stringVal();
            if (code.charAt(0) == '6')
                code = "sh." + code;
            else
                code = "sz." + code;
            return code;
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }

    public static class TimeFormat implements ObjectDeserializer
    {
        @Override
        public String deserialze (DefaultJSONParser defaultJSONParser,
                                Type type,
                                Object o)
        {
            Integer time = defaultJSONParser.getLexer().integerValue().intValue();
            String value = time.toString();
            if (value.length() == 5)
                value = "0" + value;
            value = value.replaceAll("(.{2})", ":$1").substring(1);
            return value;
            //return Time.valueOf(value);
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }

    public static class LocaleDateFormat implements ObjectDeserializer
    {
        @Override
        public LocalDate deserialze (DefaultJSONParser defaultJSONParser,
                                     Type type,
                                     Object o)
        {
            String date = defaultJSONParser.getLexer().stringVal().split(" ")[0];
            return LocalDate.parse(date);
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }
    
    public static class LocaleTimeFormat implements ObjectDeserializer
    {
        @Override
        public LocalTime deserialze (DefaultJSONParser defaultJSONParser,
                                     Type type,
                                     Object o)
        {
            Integer time = defaultJSONParser.getLexer().integerValue().intValue();
            String value = time.toString();
            if (value.length() == 5)
                value = "0" + value;
            value = value.replaceAll("(.{2})", ":$1").substring(1);
            return LocalTime.parse(value);
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }

    public static class LongFormat implements ObjectDeserializer
    {
        @Override
        public Long deserialze (DefaultJSONParser defaultJSONParser,
                                 Type type,
                                 Object o)
        {
            String val = defaultJSONParser.getLexer().stringVal();
            long value = 0;
            if (!val.isEmpty()) {
                Float floatValue = Float.parseFloat(val);
                value = floatValue.longValue();
            }
            return value;
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }

    public static class PercentFormat implements ObjectDeserializer
    {
        @Override
        public Float deserialze (DefaultJSONParser defaultJSONParser,
                                Type type,
                                Object o)
        {
            String val = defaultJSONParser.getLexer().stringVal();
            return Float.parseFloat(val) * 100;
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }

    public static class ThousandFormat implements ObjectDeserializer
    {
        @Override
        public Double deserialze (DefaultJSONParser defaultJSONParser,
                                 Type type,
                                 Object o)
        {
            int val = defaultJSONParser.getLexer().intValue();
            return val / 1000d;
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }

    public static class DateFormat implements ObjectDeserializer
    {
        @Override
        public LocalDate deserialze (DefaultJSONParser defaultJSONParser,
                                     Type type,
                                     Object o)
        {
            long val = defaultJSONParser.getLexer().longValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(val), ZoneId.systemDefault()).toLocalDate();
        }

        @Override
        public int getFastMatchToken ()
        {
            return 0;
        }
    }
}
