package com.logViz.dataservice;

import com.logViz.dataservice.datasource.connection.impl.PostgresConnection;
import com.logViz.dataservice.model.DataSource;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class PostgresConnectionTest {

    private static final String HOST = "host";
    private static final String PORT = "port";
    private static final String DATABASE = "database";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";


    @Test
    public void testPostgresConnection() {
        DataSource dataSource = DataSource.builder()
                .params(Map.of(PostgresConnectionTest.HOST,  "18.224.6.247",
                        PostgresConnectionTest.PORT, "5432",
                        PostgresConnectionTest.USERNAME, "logviz",
                        PostgresConnectionTest.PASSWORD, "logviz",
                        PostgresConnectionTest.DATABASE, "logviz"))
                .build();
        PostgresConnection connection = new PostgresConnection(dataSource);
        assert connection.testConnection().getTestSuccessful();
    }

}
