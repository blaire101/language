package com.x.p5class

/**
  * Date : 2016-04-05
  *
  * desc : 主构造器 与 类定义 交织在一起, 主构造器不以 this 来命名
  */

class Person(private var name: String, private var age: Int) {
  println("Just constructed another person")
  def description = name + " is " + age + " years old"
}

//class Person(val name: String, val age: Int) {
//  println("Just constructed another person")
//  def description = name + " is " + age + " years old"
//}

/** 多样化的声明 **/
//class Person(val name: String, private var age: Int) {
//  println("Just constructed another person")
//  def description = name + " is " + age + " years old"
//
//}

/** private[this]val 对象私有效果 **/
//class Person(name: String, age: Int) {
//  println("Just constructed another person")
//  def description = name + " is " + age + " years old"
//
//}

/** 主 构造器 私有 **/
//class Person private(val id: Int) {
//    ...
//}