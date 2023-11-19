package com.logviz.dataservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSource {
    private Integer id;
    private String name;
    private Integer orgId;
    private String connectionType;
    private Integer queryCount;
    private Integer metricCount;
    private String status;
    private Boolean isFavorite;
    private Map<String, String> params;
}