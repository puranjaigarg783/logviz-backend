package com.logViz.dataservice.repository;

import com.logViz.dataservice.model.LogEntry;

import java.util.List;

public interface LogEntryRepository {
    void insert(LogEntry logEntry);
    List<LogEntry> fetchAll();

}


