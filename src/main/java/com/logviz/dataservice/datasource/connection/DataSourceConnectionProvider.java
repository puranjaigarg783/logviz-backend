package com.logViz.dataservice.datasource.connection;

import com.logViz.dataservice.model.DataSource;
import org.apache.commons.lang3.StringUtils;

import com.logViz.dataservice.datasource.connection.impl.PostgresConnection;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class DataSourceConnectionProvider {

    private static final String POSTGRES = "POSTGRES";



    public static DataSourceConnection getDataSourceConnection(DataSource dataSource) {
        if (StringUtils.equalsIgnoreCase(POSTGRES, dataSource.getConnectionType()))
            return new PostgresConnection(dataSource);

        log.warn("No suitable dataSource connection found for : {}", dataSource.getConnectionType());
        return null;
    }

}
