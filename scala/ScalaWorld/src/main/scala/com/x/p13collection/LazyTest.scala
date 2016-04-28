package com.x.p13collection

import scala.math.pow
/**
  * Date : 2016-04-10
  */
object LazyTest {

  def main(args: Array[String]) {

    // lazy 视图
    val powers = (0 until 1000).view.map(pow(10, _))

    println(powers)

    println(powers(100)) // 10^100 被计算,其他值没有被计算

    println("\nlazy collection : ")
    // lazy collection
    // 先执行前面map, 产生中间集合, 再执行后面的 map
    (0 to 5).map(pow(10, _)).map(1 / _).foreach(println)

    println("\nlazy view : ")
    // lazy view
    // 产出的是记住两个 map 操作的view.
    // 当求值动作被强制执行时, 对于每个元素, 这两个操作被同时执行, 不需要额外构建中间集合
    (0 to 5).view.map(pow(10, _)).map(1 / _).force.foreach(println)

    // 当然,你也可以这样,对于这个简单的例子来说
    (0 to 5).map(x => pow(10, -x))

    println("\n0 to 5 : ")
    (0 to 5).foreach(println)
  }
}
