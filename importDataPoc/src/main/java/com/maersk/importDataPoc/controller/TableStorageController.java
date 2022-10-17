package com.maersk.importDataPoc.controller;

import com.maersk.importDataPoc.service.TableStorageConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TableStorageController {

    @Autowired
    private TableStorageConnector tableStorageConnector;

    @PostMapping("/addEntity")
    public void addTableEntity(){
        tableStorageConnector.addTableEntity();
    }

    @PostMapping("/addEntityList")
    public void addTableEntityList(){
        tableStorageConnector.addEntityList();
    }
}
