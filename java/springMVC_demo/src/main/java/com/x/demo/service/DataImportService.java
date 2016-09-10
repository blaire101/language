package com.x.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataImportService {

    private static DataImportService instance = new DataImportService();

    public static DataImportService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(DataImportService.class);

    private DataImportService() {
    }

    public void runImportShopInfo() {

        logger.info("runImportShopInfo start...");

        logger.info("runImportShopInfo end !");

    }

}
