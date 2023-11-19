package com.logviz.dataservice.connection;

import com.logViz.dataservice.datasource.connection.impl.PostgresConnection;
import com.logViz.dataservice.model.DataSource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
