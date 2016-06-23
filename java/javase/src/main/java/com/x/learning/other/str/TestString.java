package com.x.learning.other.str;

/**
 * Date : 2016-05-17
 */
public class TestString {
    public static void main(String[] args) {
        String s1 = "hello";
        String s2 = "world!";
        String s3 = "hello";
        System.out.println(s1 == s3); // true

        s1 = new String("hello");
        s2 = new String("hello");

        System.out.println(s1 == s3); // false
        System.out.println(s1.equals(s3)); // true

        char c[] = {'s', 'u', 'n', ' ', 'j', 'a', 'v', 'a'};
        String s4 = new String(c);
        String s5 = new String(c, 4, 3); // 数组， 下标， 长度
        System.out.println(s4 + '\n' + s5);

        System.out.println(s4.charAt(1));
        System.out.println(s4.length());
        System.out.println(s4.indexOf("java"));

        System.out.println(s4.replaceAll("sun", "oracle"));

        // Character.isLowerCase(c)
    }
}