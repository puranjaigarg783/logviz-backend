package com.logViz.dataservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEntry {
    private int id;
    private LocalDate date;
    private LocalTime time;
    private String ip;
    private String processName;
    private int processId;
    private String message;
    private String logLevel;
    private String threadInfo;
    private String exceptionName;
}
