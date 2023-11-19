package com.logViz.dataservice.repository;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TestRepository {

    private final NamedParameterJdbcTemplate     jdbcTemplate;

    public TestRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int test(){
        String sql = "select test from logviz.test where test = :test";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("test", 1);
        return jdbcTemplate.queryForObject(sql,parameters,Integer.class);
    }
}
