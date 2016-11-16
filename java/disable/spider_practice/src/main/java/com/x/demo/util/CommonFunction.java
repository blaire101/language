package com.x.demo.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * Date : 2016-06-08
 */
public class CommonFunction {

    private static Logger logger = LoggerFactory.getLogger(CommonFunction.class);

    public static char aChar = '#';

    public static int count = 0;


    public static void showCurDate() {
        logger.info("current date : {}" + new Date());
    }

    public static void sleepSec(long seconds) {
        try {
            Thread.currentThread().sleep((seconds));
        } catch (InterruptedException e) {
            logger.error("InterruptedException");
        }
    }

    public static long getSmallRandom() {
        double x = Math.random() * 2;
        int intervalSeconds = Integer.parseInt(new java.text.DecimalFormat("0").format(x)) + 1;
        return (long)(intervalSeconds * Constant.MS);
    }

    public static long getRandom() {
        double x = Math.random() * 5;
        int intervalSeconds = Integer.parseInt(new java.text.DecimalFormat("0").format(x)) + 2;
        return (long)(intervalSeconds * Constant.MS);
    }

    public static void decur(String s) {
        if (s.length() == Constant.YL) {
            return;
        }
        String res = "";
        for (int i = 97; i <= 122; i++) {
            res = s + String.valueOf((char) i);
            if (Constant.YL == res.length()) {
                ++count;
                CommonFunction.isSleep(count);
                if (res.charAt(1) != aChar) {
                    logger.info("res : {}", res);
                    aChar = res.charAt(1);
                    CommonFunction.sleepSec(CommonFunction.getRandom());
                }
//                System.out.println("res : " + res);
                getUrlResult(res);
            }
            decur(res);
        }
    }

    private static void isSleep(int count) {
        if (count % Constant.ISTT == 0) {
            CommonFunction.sleepSec(CommonFunction.getRandom());
        }
        if (count % Constant.ISTWO == 0) {
            CommonFunction.sleepSec(CommonFunction.getRandom());
        }
        if (count % Constant.IST == 0) {
            CommonFunction.sleepSec(CommonFunction.getRandom());
        }
    }

    public static String getUrlResult(String domain) {
        // TODO Auto-generated method stub
//		  String url = "http://www.ituring.com.cn/tupubarticle/5567";
//        String url = "http://sg.godaddy.com/zh/domains/searchresults.aspx?checkAvail=1&domainToCheck=gorobbybean";

        String fullUrl = Constant.XINNET_PREFIX + domain + Constant.XINNET_SUFFIX;

        try {
            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("top", "4");
            Document doc = Jsoup.connect(fullUrl).header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36").data(dataMap).ignoreContentType(true).timeout(20 * 1000).post();
            String resHTml = doc.html();
//            System.out.println("html : " + resHTml);
            if (!resHTml.contains(Constant.WA)) {
//                System.out.println("dm " + domain);
                logger.info(Constant.WWW + domain + Constant.COM);
            }
            return doc.html();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

}
