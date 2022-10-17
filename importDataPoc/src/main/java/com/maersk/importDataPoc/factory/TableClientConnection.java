package com.maersk.importDataPoc.factory;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TableClientConnection {

    /*private final String connectionString =
            "DefaultEndpointsProtocol=https;" +
                    "AccountName=saautomatedianconfigdev;" +
                    "AccountKey=p3DqcT2tNNJTZOSp0RowiVpV6Cx0yPxAYobqZ8Wgz0CCzugwr1FlzraShY72UYVNpSYGkUOgGmnT+AStqzoEgg==;" +
                    "EndpointSuffix=core.windows.net";*/

    @Autowired
    private Environment environment;

    @Value("${azure.storage.table.DefaultEndpointsProtocol}")
    private String defaultEndpointsProtocol;

    @Value("${azure.storage.table.AccountName}")
    private String accountName;

    @Value("${azure.storage.table.AccountKey}")
    private String accountKey;

    @Value("${azure.storage.table.EndpointSuffix}")
    private String endpointSuffix;


    public TableClient getTableClient(String tableName) {
        String connectionString = "DefaultEndpointsProtocol=" + defaultEndpointsProtocol + ";" +
                "AccountName=" + accountName + ";" +
                "AccountKey=" + accountKey + ";" +
                "EndpointSuffix=" + endpointSuffix + ";";
        // Create a TableClient with a connection string and a table name.
        TableClient tableClient = new TableClientBuilder()
                .connectionString(connectionString)
                .tableName(tableName)
                .buildClient();
        return tableClient;

    }
}
