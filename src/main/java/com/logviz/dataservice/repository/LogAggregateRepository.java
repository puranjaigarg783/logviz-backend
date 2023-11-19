package com.logViz.dataservice.repository;

import com.logViz.dataservice.model.LogAggregates;
import com.logViz.dataservice.model.LogEntry;

import java.util.List;

public interface LogAggregateRepository {
    void insert(LogAggregates logAggregates);

    List<LogAggregates> getAggregates(String granularity, String logLevel);
}
