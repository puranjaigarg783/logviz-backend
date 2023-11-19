package com.logViz.dataservice.repository;

import com.logViz.dataservice.model.LogEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogEntryRepositoryImpl implements LogEntryRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void insert(LogEntry logEntry) {
        String sql = "INSERT INTO logviz.log_entry(date, time, ip, process_name, process_id, message, log_level, thread_info, exception_name) " +
                "VALUES (:date, :time, :ip, :processName, :processId, :message, :logLevel, :threadInfo, :exceptionName)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("date", logEntry.getDate());
        params.addValue("time", logEntry.getTime());
        params.addValue("ip", logEntry.getIp());
        params.addValue("processName", logEntry.getProcessName());
        params.addValue("processId", logEntry.getProcessId());
        params.addValue("message", logEntry.getMessage());
        params.addValue("logLevel", logEntry.getLogLevel());
        params.addValue("threadInfo", logEntry.getThreadInfo());
        // Assuming exception_name is extracted differently
        params.addValue("exceptionName", null);

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public List<LogEntry> fetchAll() {
        String sql = "SELECT * FROM logviz.log_entry";
        return namedParameterJdbcTemplate.query(sql, (rs, rowNum) -> {
            LogEntry logEntry = new LogEntry();
            logEntry.setId(rs.getInt("id"));
            logEntry.setDate(rs.getDate("date").toLocalDate());
            logEntry.setTime(rs.getTime("time").toLocalTime());
            logEntry.setIp(rs.getString("ip"));
            logEntry.setProcessName(rs.getString("process_name"));
            logEntry.setProcessId(rs.getInt("process_id"));
            logEntry.setMessage(rs.getString("message"));
            logEntry.setLogLevel(rs.getString("log_level"));
            logEntry.setThreadInfo(rs.getString("thread_info"));
            // Add other fields as necessary
            return logEntry;
        });
    }
}



