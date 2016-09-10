package com.x.dmt.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date : 2016-07-21
 */

public class TestMainLogService {

    private static TestMainLogService instance = new TestMainLogService();

    public static TestMainLogService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(TestMainLogService.class);

    private TestMainLogService() {
    }

    public void run() {
        logger.info("haha info1");
        logger.warn("haha warn");
        logger.error("haha error1");

        logger.info("haha info2");
        logger.debug("debug haha");
        logger.error("haha error2");

    }

    public static void main(String[] args) {
        TestMainLogService.getInstance().run();
    }
}
