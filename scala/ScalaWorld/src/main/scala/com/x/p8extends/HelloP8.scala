package com.x.p8extends

/**
  * Date : 2016-04-05
  */
object HelloP8 extends App {


  println(new Employee().toString)

  // 8.3 类型检查和转换

  val employee = new Employee();

  if (employee.isInstanceOf[Employee]) {
    val s = employee.asInstanceOf[Employee] // s 的类型为 Employee
    println("is Employee, true")
  }
  // 8.4 受保护字段和方法, protected 成员对于 所属的包而言,是不可见的

  // 8.5 超类的构造

  // 8.6 重写字段

}
