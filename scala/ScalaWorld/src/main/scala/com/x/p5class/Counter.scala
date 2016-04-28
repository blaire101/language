package com.x.p5class

/**
  * Date : 2016-04-05
  */
class Counter {

  private var value = 0 // private[this] var value = 0 这是对象私有字段 某个对象.value 不允许

  def increment() { value += 1 } // function default is public

  def current = value  // 取值器

  def isLess(other: Counter) = value < other.value

}