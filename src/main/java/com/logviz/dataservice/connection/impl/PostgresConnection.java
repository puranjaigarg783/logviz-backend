package com.logviz.dataservice.connection.impl;

import com.logViz.dataservice.common.exception.DataSourceException;
import com.logViz.dataservice.datasource.connection.DataSourceConnection;
import com.logViz.dataservice.model.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class PostgresConnection implements DataSourceConnection {

    private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String DATABASE = "database";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String JDBC_URL_FORMAT = "jdbc:postgresql://%s:%s/%s";
    private static final String TEST_QUERY = "SELECT 1";
    public static final String SELECT_TOP_50_ROWS_INNER_QUERY_KEY = "<QUERY>";
    private static final String SELECT_TOP_50_ROWS_OUTER_QUERY = "select pulsops_query_run_data.* from \n" +
            " ( " + SELECT_TOP_50_ROWS_INNER_QUERY_KEY + " ) pulsops_query_run_data\n" +
            " limit 50";
    private static final String IMPROPER_POSTGRESQL_QUERY_EXCEPTION_MESSAGE = "Postgresql failed to execute query, please check the query and try again";


    private final HikariDataSource hikariDataSource;
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    private static final List<String> POSTGRES_CONNECTION_KEYS = List.of(HOST, PORT, DATABASE, USERNAME, PASSWORD);

    @Override
    public void validate() {
        List<String> missingKeys = POSTGRES_CONNECTION_KEYS.stream()
                .filter(key -> !this.dataSource.getParams().containsKey(key))
                .collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(missingKeys)) {
            throw new IllegalStateException(missingKeys + " params are mandatory for a Postgres Connection");
        }
    }

    public PostgresConnection(DataSource dataSource) {
        this.dataSource = dataSource;
        log.info("Building Postgres connection for :{}", dataSource);
        Map<String, String> params = dataSource.getParams();
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(String.format(JDBC_URL_FORMAT, params.get(HOST), params.get(PORT), params.get(DATABASE)));
        hikariDataSource.setDriverClassName(DRIVER_CLASS_NAME);
        hikariDataSource.setUsername(params.get(USERNAME));
        hikariDataSource.setPassword(params.get(PASSWORD));
        hikariDataSource.setIdleTimeout(20000);
        hikariDataSource.setMaximumPoolSize(1);
        this.hikariDataSource = hikariDataSource;
        this.jdbcTemplate = new JdbcTemplate(this.hikariDataSource);
    }

    @Override
    public TestConnectionResult testConnection() {
        TestConnectionResult result;
        try {
            this.jdbcTemplate.queryForList(TEST_QUERY);
            log.info("Datasource connection successful");
            result = TestConnectionResult.builder()
                    .testSuccessful(true)
                    .build();
        } catch (Exception e) {
            log.info("Test connection failed wih exception: {}, cause: {}", e.getClass(), e.getCause());
            result = TestConnectionResult.builder()
                    .testSuccessful(false)
                    .errorMessage(e.getCause().getMessage())
                    .build();
        } finally {
            this.hikariDataSource.close();
        }
        return result;
    }

    @Override
    public QueryResult getQueryRunData(QueryRunMsg queryRunMsg, boolean limitRows) {
        QueryResult queryResult = new QueryResult();
        try{
            String query = limitRows ? buildQueryWithLimit(queryRunMsg.getQuery()) : queryRunMsg.getQuery();
            List<Map<String,Object>> rowSet =this.jdbcTemplate.queryForList(query);
            if(CollectionUtils.isEmpty(rowSet)) {
                throw new DataSourceException("Query did not return any result");
            }
            queryResult.setRows(rowSet);
            List<String> columns = new ArrayList<>(rowSet.get(0).keySet());
            queryResult.setColumns(columns);
        } catch (DataAccessException | DataSourceException e){
            log.info("Query Run failed wih client side exception: {}, cause: {}", e.getClass(), e.getCause());
            throw new IllegalArgumentException("Query run failed with error : " +
                    (StringUtils.isNotBlank(e.getCause().getMessage()) ? e.getCause().getMessage() : IMPROPER_POSTGRESQL_QUERY_EXCEPTION_MESSAGE));
        } catch (Exception e){
            log.info("Query Run failed wih exception: {}, cause: {}", e.getClass(), e.getCause());
        } finally {
            this.hikariDataSource.close();
        }
        return queryResult;
    }


    private String buildQueryWithLimit(String query) {
        String newQuery = query.trim();
        if(newQuery.charAt(newQuery.length() - 1) == ';') {
            newQuery = newQuery.substring(0, newQuery.length() - 1);
        }
        return SELECT_TOP_50_ROWS_OUTER_QUERY.replaceAll(SELECT_TOP_50_ROWS_INNER_QUERY_KEY, newQuery);
    }


    @Override
    public HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
