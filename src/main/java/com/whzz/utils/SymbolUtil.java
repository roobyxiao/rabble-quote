package com.whzz.utils;

public class SymbolUtil {
    public static boolean isHs(String code)
    {
        return code.startsWith("sh.60") || code.startsWith("sz.00") || code.startsWith("sz.30");
    }

    public static boolean isHsZB(String code)
    {
        return code.startsWith("sh.60") || code.startsWith("sz.00");
    }

    public static String sbSymbolToCode(String symbol)
    {
        return symbol.substring(0, 2).toLowerCase() + "." + symbol.substring(2);

    }

    public static String codeToSbSymbol(String code)
    {
        return code.replaceAll("\\.", "").toUpperCase();
    }

    public static String symbolToCode(String symbol)
    {
        return symbol.startsWith("60") ? "sh." + symbol : "sz." + symbol;

    }

    public static String codeToSymbol(String code)
    {
        return code.split("\\.")[1];
    }

    public static String tsCodeToCode(String tsCode)
    {
        String[] codes = tsCode.toLowerCase().split("\\.");
        String  code = codes[1].toLowerCase() + "." + codes[0];
        return code;
    }

    public static String codeToTsCode(String code)
    {
        var codes = code.toLowerCase().split("\\.");
        var tsCode = codes[1] + "." + codes[0].toUpperCase();
        return tsCode;
    }

    public static String getSecId(String code)
    {
        String[] codes = code.split("\\.");
        return ("sh".equalsIgnoreCase(codes[0]) ? 1 : 0) + "." + codes[1];
    }
}
