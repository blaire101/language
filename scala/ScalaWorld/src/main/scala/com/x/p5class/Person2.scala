package com.x.p5class

/**
  * Date : 2016-04-05
  */
class Person2 {
  private var name = ""
  private var age = 0

  def this(name: String) { // auxiliary comstructor
    this()
    this.name = name
  }

  def this(name: String, age: Int) { // Another auxiliary constructor
    this(name)
    this.age = age
  }

  override def toString = getClass.getName + "[name=" + name + "]"
}
