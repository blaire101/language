package com.x.demo.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainTest {
    public static void main(String[] args) {

        Date as1 = new Date(new Date().getTime()-0*24*60*60*1000);
        System.out.println(as1);

        Date as = new Date(new Date().getTime()-24*60*60*1000);
        System.out.println(as);
        SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
        String time = matter1.format(as);
        System.out.println(time);

        System.out.println(Long.valueOf("172"));
        System.out.println("---");

//        System.out.println(new TaskManager().getContext(""));
//        System.out.println("shopId");

//        System.out.println(new TaskManager().getShopId("/config?_method=GET&_timeStamp=1447627484799&busiId=172&busiType=shop"));

        Date currentTime = new Date();
        String dateString = new SimpleDateFormat("_yyyy-MM-dd").format(new Date());
        System.out.println(dateString);

        String tm = "28/Nov/2014:11:56:09 +0800";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(tm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("转换后的日期格式："+format.format(date));
    }
}
