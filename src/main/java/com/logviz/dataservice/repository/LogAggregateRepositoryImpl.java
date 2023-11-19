package com.logViz.dataservice.repository;

import com.logViz.dataservice.model.LogAggregates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LogAggregateRepositoryImpl implements LogAggregateRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void insert(LogAggregates logAggregates) {
        String sql = "INSERT INTO logviz.log_aggregates(date, hour, log_level, count, most_frequent_ip, avg_message_length, max_message_length, unique_processes_count, most_common_exception, most_active_thread) " +
                "VALUES (:date, :hour, :logLevel, :count, :mostFrequentIp, :avgMessageLength, :maxMessageLength, :uniqueProcessesCount, :mostCommonException, :mostActiveThread)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("date", logAggregates.getDate());
        params.addValue("hour", logAggregates.getHour());
        params.addValue("logLevel", logAggregates.getLogLevel());
        params.addValue("count", logAggregates.getCount());
        params.addValue("mostFrequentIp", logAggregates.getMostFrequentIp());
        params.addValue("avgMessageLength", logAggregates.getAvgMessageLength());
        params.addValue("maxMessageLength", logAggregates.getMaxMessageLength());
        params.addValue("uniqueProcessesCount", logAggregates.getUniqueProcessesCount());
        params.addValue("mostCommonException", logAggregates.getMostCommonException());
        params.addValue("mostActiveThread", logAggregates.getMostActiveThread());

        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public List<LogAggregates> getAggregates(String granularity, String logLevel) {
        String sql;
        if(logLevel == null) {
             sql = "select * from logviz.get_log_aggregates(:granularity)";
        }
        else {
             sql = "select * from logviz.get_log_aggregates(:granularity) where log_level = :logLevel";
        }
//        String sql = "select * from logviz.get_log_aggregates(:granularity) where log_level = :logLevel";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("granularity", granularity)
                .addValue("logLevel", logLevel);

        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            LogAggregates logAggregates = new LogAggregates();
            logAggregates.setId(rs.getInt("id"));
            logAggregates.setDate(rs.getDate("date").toLocalDate());
            logAggregates.setHour(rs.getInt("hour"));
            logAggregates.setLogLevel(rs.getString("log_level"));
            logAggregates.setCount(rs.getInt("count"));
            logAggregates.setMostFrequentIp(rs.getString("most_frequent_ip"));
            logAggregates.setAvgMessageLength(rs.getInt("avg_message_length"));
            logAggregates.setMaxMessageLength(rs.getInt("max_message_length"));
            logAggregates.setUniqueProcessesCount(rs.getInt("unique_processes_count"));
            logAggregates.setMostCommonException(rs.getString("most_common_exception"));
            logAggregates.setMostActiveThread(rs.getString("most_active_thread"));
            return logAggregates;
        });
        }
        }

