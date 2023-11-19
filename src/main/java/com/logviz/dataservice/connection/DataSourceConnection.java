package com.logviz.dataservice.connection;

import com.logViz.dataservice.datasource.connection.impl.QueryResult;
import com.logViz.dataservice.datasource.connection.impl.QueryRunMsg;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

public interface DataSourceConnection {

    HikariDataSource getHikariDataSource();

    JdbcTemplate getJdbcTemplate();

    void validate();

    TestConnectionResult testConnection();
    
    QueryResult getQueryRunData(QueryRunMsg QueryRunMsg, boolean limitRows);
  
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    class TestConnectionResult {
        private Boolean testSuccessful;
        private String errorMessage;
    }
    

}
