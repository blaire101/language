package com.x.demo.service;

import com.x.demo.util.CommonFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date : 2016-06-23
 */

public class SpiderCoreService {

    public static SpiderCoreService instance = new SpiderCoreService();

    public static SpiderCoreService getInstance() {
        return instance;
    }

    private static Logger logger = LoggerFactory.getLogger(SpiderCoreService.class);

    public void run() {

        logger.info("start...");
        CommonFunction.showCurDate();

        int ca = 0;

        String url = "asd6513293213";

        CommonFunction.getUrlResult(url);
//
        for (int i = 98; i <= 122; i++) {
            String tmp = String.valueOf((char) i);
            logger.info("ca : {}", (++ca));
            CommonFunction.decur(tmp);
        }
        logger.info("end !!!!!");
    }

    public static void main(String[] args) {
        SpiderCoreService.getInstance().run();
    }


}
