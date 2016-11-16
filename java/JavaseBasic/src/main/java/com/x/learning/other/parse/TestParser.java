package com.x.learning.other.parse;

/**
 * Date : 2016-05-17
 */
public class TestParser {
    public static void main(String[] args) {
        Integer i = new Integer(100);
        Double d = new Double("123.456");

        int j = i.intValue() + d.intValue();
        float f = i.floatValue() + d.floatValue();

        System.out.println(j);
        System.out.println(f);

        double pi = Double.parseDouble("3.1415926");
        double r = Double.valueOf("2.0").doubleValue();
        double s = pi * r * r;
        System.out.println(s);

        try {
            int k = Integer.parseInt("1.25");
        } catch (NumberFormatException e) {
            System.out.println("数据格式不对");
        }

        System.out.println(Integer.toBinaryString(123) + "B");
        System.out.println(Integer.toHexString(123) + "H");
        System.out.println(Integer.toOctalString(123) + "O");
    }

}
