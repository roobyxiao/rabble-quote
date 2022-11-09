package com.whzz.applicationsrevice.quote.dto;

import lombok.Data;

@Data
public class EmLimitDto {
    private String c;
    private String fbt;
    private String lbt;
    private int zbc;
    private int lbc;
    private Zttj zttj;

    @Data
    public static class Zttj {
        private int ct;
        private int days;
    }
}

