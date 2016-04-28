package com.x.p5class

/**
  * Date : 2016-04-05
  */
class Person1 {
  private var privateAge = 0

  def age = privateAge
  def age_=(newValue: Int): Unit = {
    if (newValue > privateAge) { // can't become young
      privateAge = newValue
    }
  }
}
