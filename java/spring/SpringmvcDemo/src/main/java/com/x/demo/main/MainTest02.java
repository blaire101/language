package com.x.demo.main;

import java.io.File;
import java.io.IOException;

/**
 * Date : 2016-04-11
 */
public class MainTest02 {
  public static void main(String[] args) throws IOException {

//    File file = new File("jsonFile/jdCategoryTree.json");
//    System.out.println(file.getAbsolutePath());
    TaskManager.getInstance().run();
  }
}
