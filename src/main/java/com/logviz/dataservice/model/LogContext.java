package com.logviz.dataservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class LogContext {

    private int id;

    private LogEntry logEntry;

    private String applicationName;
    private String environment;
    private String region;
    private String otherContext;
}
