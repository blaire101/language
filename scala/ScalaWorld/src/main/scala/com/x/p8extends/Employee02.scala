package com.x.p8extends

import com.x.p5class.Person2

/**
  * Date : 2016-04-05
  *
  * 超类的构造
  *
  * 辅构造器 只能 调用 辅/主 构造器, 但不能调用 超构造器, 主构造器 可以 调用 超构造器
  */
class Employee02(name: String, age: Int, val salary: Double) extends Person2(name, age){
  // 重写方法
  override def toString = super.toString + "[salary=" + salary + "]"
}
