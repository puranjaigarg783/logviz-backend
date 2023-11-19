package com.logviz.dataservice.connection.impl;

public class QueryRunMsg {

    Integer data_source_id;
    String query;

    public QueryRunMsg() {

    }

    public Integer getData_source_id() {
        return data_source_id;
    }

    public void setData_source_id(Integer data_source_id) {
        this.data_source_id = data_source_id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryRunMsg(Integer data_source_id, String query) {
        this.data_source_id = data_source_id;
        this.query = query;
    }
}
