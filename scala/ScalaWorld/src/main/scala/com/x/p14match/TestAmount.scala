package com.x.p14match

/**
  * Date : 2016-05-05
  */
object TestAmount {

  def main(args: Array[String]) {
  }
}

abstract class Amount
// 继承了普通类的两个样例类
case class Dollar(value: Double) extends Amount
case class Currency(value: Double, unit: String) extends Amount

// 样例对象
case object Nothing extends Amount