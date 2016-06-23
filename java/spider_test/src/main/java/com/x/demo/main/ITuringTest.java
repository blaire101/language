package com.x.demo.main;

import com.x.demo.util.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * 1. 每扫描 676 个, 睡眠 5~8秒
 * 2. 扫描 长度 为 4 的域名
 * test_practice
 */
public class ITuringTest {

    public static void main(String[] args) {

        double x = Math.random() * 5;
        int intervalSeconds = Integer.parseInt(new java.text.DecimalFormat("0").format(x)) + 2;
        System.out.println(intervalSeconds);

        int ca = 0;

        System.out.println("start...");
        String url = "gorobbybean1";

        System.out.println(new Date());
        try {
            Thread.currentThread().sleep((long)intervalSeconds * 1000);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
//        ITuring.getUrlResult(url, Constant.COM);

//        for (int i = 97; i <= 122; i++) {
//            String tmp = String.valueOf((char) i);
////            System.out.println("ca : " + (++ca));
//            for (int j = 97; j <= 122; j++) {
//                String tj = String.valueOf((char) j);
//                url = "bc" + tmp + tj;
//                System.out.println("url : " + url);
//                ITuring.getUrlResult(url, Constant.COM);
//            }
//
//        }
        System.out.println("end!!!");
    }

}
