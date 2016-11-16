package com.x.learning.model;

/**
 * Date : 2016-05-31
 */
public class Person {
    private Long id = 1L;
    private String name;

    public Person(Long id, String name) {

        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
