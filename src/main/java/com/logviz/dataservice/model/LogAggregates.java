package com.logviz.dataservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAggregates {

    private int id;

    private LocalDate date;

    private Integer hour;

    private String logLevel;
    private int count;
    private String mostFrequentIp;
    private int avgMessageLength;
    private int maxMessageLength;
    private int uniqueProcessesCount;
    private String mostCommonException;
    private String mostActiveThread;
}