package com.whzz.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document("tick")
public class Tick{
    @Id
    private ObjectId id;
    private String code;
    private String date;
    private List<TickData> data;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TickData {
        private String t;
        private double p;
        private long v;
        private int bs;
    }
}

