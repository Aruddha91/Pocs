package com.maersk.importDataPoc.service;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableClientBuilder;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.TableEntity;
import com.azure.data.tables.models.TableTransactionAction;
import com.azure.data.tables.models.TableTransactionActionType;
import com.maersk.importDataPoc.factory.TableClientConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.*;

@Component
public class TableStorageConnector {

    private int rowKey = 65;//todo : generate a new row key

    @Autowired
    private TableClientConnection tableClientConnection;

    public void addTableEntity() {

        try {
            final String tableName = "ImportData";

            // Create a TableClient with a connection string and a table name.
            TableClient tableClient = tableClientConnection.getTableClient(tableName);

            // Create a new employee TableEntity.
            String partitionKey = "LastName";
            String rowKey = "0002";
            Map<String, Object> personalInfo = new HashMap<>();
            personalInfo.put("FirstName", "Walter");
            personalInfo.put("LastName", "Harp");
            personalInfo.put("Email", "Walter@contoso.com");
            personalInfo.put("PhoneNumber", "425-555-0101");
            TableEntity employee = new TableEntity(partitionKey, rowKey).setProperties(personalInfo);

            // Upsert the entity into the table
            tableClient.upsertEntity(employee);
        } catch (Exception e) {
            // Output the stack trace.
            e.printStackTrace();
        }
    }

    public void addEntityList(){

        try
        {
            final String tableName = "ImportData";

            TableClient tableClient = tableClientConnection.getTableClient(tableName);

            String partitionKey = "LastName";
            List<TableTransactionAction> tableTransactionActions = new ArrayList<>();

            Map<String, Object> personalInfo1 = new HashMap<>();
            personalInfo1.put("FirstName", "Jeff");
            personalInfo1.put("LastName", "Smith");
            personalInfo1.put("Email", "Jeff@contoso.com");
            personalInfo1.put("PhoneNumber", "425-555-0104");

            // Create an entity to add to the table.
            tableTransactionActions.add(new TableTransactionAction(
                    TableTransactionActionType.UPSERT_MERGE,
                    new TableEntity(partitionKey, "0005")
                            .setProperties(personalInfo1)
            ));

            Map<String, Object> personalInfo2 = new HashMap<>();
            personalInfo2.put("FirstName", "Ben");
            personalInfo2.put("LastName", "Johnson");
            personalInfo2.put("Email", "Ben@contoso.com");
            personalInfo2.put("PhoneNumber", "425-555-0102");

            // Create another entity to add to the table.
            tableTransactionActions.add(new TableTransactionAction(
                    TableTransactionActionType.UPSERT_MERGE,
                    new TableEntity(partitionKey, "0003")
                            .setProperties(personalInfo2)
            ));

            Map<String, Object> personalInfo3 = new HashMap<>();
            personalInfo3.put("FirstName", "Denise");
            personalInfo3.put("LastName", "Rivers");
            personalInfo3.put("Email", "Denise@contoso.com");
            personalInfo3.put("PhoneNumber", "425-555-0103");

            // Create a third entity to add to the table.
            tableTransactionActions.add(new TableTransactionAction(
                    TableTransactionActionType.UPSERT_MERGE,
                    new TableEntity(partitionKey, "0004")
                            .setProperties(personalInfo3)
            ));

            // Submit transaction on the "Employees" table.
            tableClient.submitTransaction(tableTransactionActions);
        }
        catch (Exception e)
        {
            // Output the stack trace.
            e.printStackTrace();
        }
    }


    public void addEntityList(List<Map<String, Object>> excelRowList, String tableName, String partitionKey){

        // Create a TableClient with a connection string and a table name.
        TableClient tableClient = tableClientConnection.getTableClient(tableName);
        List<TableTransactionAction> tableTransactionActions = new ArrayList<>();
        excelRowList.forEach(ex -> tableTransactionActions.add(
                new TableTransactionAction(TableTransactionActionType.UPSERT_MERGE,
                        new TableEntity(partitionKey, UUID.randomUUID().toString()).setProperties(ex))));

        tableClient.submitTransaction(tableTransactionActions);
    }

    public void addEntityList(List<Map<String, Object>> excelRowList){
        final String tableName = "ImportData";
        String partitionKey = "LastName";

        // Create a TableClient with a connection string and a table name.
        TableClient tableClient = tableClientConnection.getTableClient(tableName);
        List<TableTransactionAction> tableTransactionActions = new ArrayList<>();
        excelRowList.forEach(ex -> tableTransactionActions.add(
                new TableTransactionAction(TableTransactionActionType.UPSERT_MERGE,
                        new TableEntity(partitionKey, UUID.randomUUID().toString()).setProperties(ex))));

        tableClient.submitTransaction(tableTransactionActions);
    }

}
