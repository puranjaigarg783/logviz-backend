package com.logViz.dataservice.datasource.connection.impl;

import java.util.List;
import java.util.Map;

public class QueryResult {
    List<String> columns;
    List<Map<String,Object>> rows;



    public QueryResult(List<String> columns) {
        this.columns = columns;
    }

    public QueryResult() {

    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public QueryResult(List<String> columns, List<Map<String, Object>> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }
}
