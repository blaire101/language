package com.x.learning.other.str;

/**
 * Date : 2016-05-17
 */

public class TestBuffer {
    public static void main(String[] args) {
        String s = "Mircosoft";
        char arr[] = {'a', 'b', 'c'};
        StringBuffer sb1 = new StringBuffer(s);
        sb1.append('/').append("IBM");
        System.out.println(sb1);
        StringBuffer sb2 = new StringBuffer("数字");
        for (int i = 0; i <= 9; i++) {
            sb2.append(i);
        }
        System.out.println(sb2);

        sb2.delete(8, 11).insert(0, arr);

        System.out.println(sb2);
        System.out.println(sb2.reverse());
    }
}