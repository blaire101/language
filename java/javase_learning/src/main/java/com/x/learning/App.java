package com.x.learning;

import com.alibaba.fastjson.JSON;
import com.x.learning.model.Person;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        System.out.println("Hello World!");
        Person person = new Person(8L, null);

        String res = JSON.toJSON(person).toString();

        System.out.println(res);
    }
}

