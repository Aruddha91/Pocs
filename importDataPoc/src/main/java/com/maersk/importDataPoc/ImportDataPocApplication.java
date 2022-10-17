package com.maersk.importDataPoc;

import com.maersk.importDataPoc.controller.TableStorageController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ImportDataPocApplication {

	public static void main(String[] args) {
		System.out.println("ImportDataPocApplication Starting... ");
		SpringApplication.run(ImportDataPocApplication.class, args);
		System.out.println("ImportDataPocApplication Started... ");
	}

}
