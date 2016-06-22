package com.x.demo.main;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * test_practice
 */
public class ITuring {

    public static void main(String[] args) {

        String url = "https://wanwang.aliyun.com/domain/searchresult/?keyword=gorobby&suffix=.com";
        getUrlResult(url);

//        for(int i = 97; i <= 122; i++) {
//            String tmp = String.valueOf((char)i);
//            decur(tmp);
//        }
    }


    public static void decur(String s) {
        if(s.length() > 2) {
            return;
        }
        String res = "";
        for(int i = 97; i <= 122; i++) {
            res = s + String.valueOf((char)i);
            System.out.println(res);
            decur(res);
        }
    }

    public static String getUrlResult(String url) {
        // TODO Auto-generated method stub
//		String url = "http://www.ituring.com.cn/tupubarticle/5567";
//        String url = "http://sg.godaddy.com/zh/domains/searchresults.aspx?checkAvail=1&domainToCheck=gorobbybean";
        try {
            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("top", "4");
            Document doc = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36").data(dataMap).ignoreContentType(true).timeout(20*1000).post();
//            System.out.println(doc.html());
            String resHTml = doc.html();
            System.out.println(resHTml);
            if (resHTml.contains("已售出")) {
                System.out.println(url);
            }
            return doc.html();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

}
