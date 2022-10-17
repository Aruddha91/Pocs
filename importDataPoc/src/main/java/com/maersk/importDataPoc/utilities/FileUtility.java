package com.maersk.importDataPoc.utilities;

import com.maersk.importDataPoc.controller.ReadFileController;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class FileUtility {
    public static XSSFSheet getSheet(String excelFilePath) throws IOException {
        ClassLoader classLoader = FileUtility.class.getClassLoader();
        FileInputStream inputStream = new FileInputStream(new File(Objects.requireNonNull(classLoader.getResource(excelFilePath)).getFile()));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        return sheet;
    }
}
