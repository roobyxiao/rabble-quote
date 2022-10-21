package com.whzz.applicationsrevice.quote.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.whzz.utils.FastJsonSerializerUtil;
import lombok.Data;

@Data
public class EmTickDataDto {
    @JSONField(deserializeUsing = FastJsonSerializerUtil.TimeFormat.class)
    private String t;
    @JSONField(deserializeUsing = FastJsonSerializerUtil.ThousandFormat.class)
    private double p;
    private long v;
    private int bs;
}
