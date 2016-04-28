package com.x.p8extends

import com.x.p5class.Person2

/**
  * Date : 2016-04-05
  */
class Employee extends Person2 {
  var salary = 0.0

  // 重写方法
  override def toString = super.toString + "[salary=" + salary + "]"
}
