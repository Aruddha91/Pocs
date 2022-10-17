package com.maersk.importDataPoc.controller;

import com.maersk.importDataPoc.service.TableStorageConnector;
import com.maersk.importDataPoc.utilities.FileUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


@RestController
public class ReadFileController {

    @Autowired
    private TableStorageConnector tableStorageConnector;

    @PostMapping("/excel")
    public String excelReader(@RequestParam(defaultValue = "testExcel.xlsx") String excelFilePath ) {

        try {
            XSSFSheet sheet = FileUtility.getSheet(excelFilePath);

            for(int i=0; i<sheet.getPhysicalNumberOfRows();i++) {
                XSSFRow row = sheet.getRow(i);
                for(int j=0;j<row.getPhysicalNumberOfCells();j++) {
                    System.out.print(row.getCell(j) +" ");
                }
                System.out.println("");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "Success";
    }



    @PostMapping("/readExcel")
    public String readExcelAndStoreToAzure(@RequestParam(defaultValue = "testExcel.xlsx") String excelFilePath) {

        try {
            XSSFSheet sheet = FileUtility.getSheet(excelFilePath);

            List<String> excelKeys = new ArrayList<>();
            List<Map<String, Object>> cellKeyMapList = new ArrayList<>();
            XSSFRow keyRow =  sheet.getRow(0);

            for (int i = 0; i < keyRow.getPhysicalNumberOfCells(); i++){
                excelKeys.add(keyRow.getCell(i).toString());
            }

            for(int i=1; i<sheet.getPhysicalNumberOfRows();i++) {
                XSSFRow row = sheet.getRow(i);
                Map<String, Object> cellKeyMap = new HashMap<>();
                for(int j=0;j<row.getPhysicalNumberOfCells();j++) {
                    cellKeyMap.put(excelKeys.get(j), row.getCell(j).toString());
                }
                cellKeyMapList.add(cellKeyMap);
            }

            tableStorageConnector.addEntityList(cellKeyMapList);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "Success";
    }
}
