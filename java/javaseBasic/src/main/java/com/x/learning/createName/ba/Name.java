package com.x.learning.createName.ba;

/**
 * Date : 2016-05-21
 */
public class Name {

    public static void main(String[] args) {

        String a1[] = {
                "立"
        };
        String a3[] = {
                "峘",
                "型",
                "垣",
                "约",
                "幽",
                "垠",
                "纡",
                "垲",
                "怡",
                "音",
                "姻",
                "韦",
                "禹",
                "咿",
                "爰"
        };
        String a2[] = {
                "朵",
                "伎",
                "聿",
                "各",
                "戎",
                "竹",
                "曲",
                "企"
        };

        for (String fn : a3) {
            for (String ln : a2) {
                System.out.println("" + fn + ln);
                System.out.println("" + ln + fn);
            }
        }
//        for (String fn : a1) {
//            for (String ln : a2) {
//                System.out.println("" + ln + fn);
//            }
//        }
    }
}
