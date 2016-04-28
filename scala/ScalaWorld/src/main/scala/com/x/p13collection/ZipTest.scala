package com.x.p13collection

/**
  * Date : 2016-04-10
  */
object ZipTest extends App {
  val prices = List(5.0, 20.0, 9.95)
  val quantities = List(10, 2, 1)

  val zipres = prices zip quantities

  zipres.foreach(println)

  (zipres map { p => p._1 * p._2 }) sum


  // 如果一个 集合 比 另一个短,那么集合的元素数量是短的那个

  // zipAll 方法, 让你指定较短列表的缺省值. 如下是 : 1
  println("zipAll : ")
  List(5.0, 20.0, 9.95).zipAll(List(10), 2.0, 1).foreach(println)

  // zipWithIndex 方法返回对偶的列表
  println("zipWithIndex : ")

  println("Scala".zipWithIndex.max)

  println("Scala".zipWithIndex.max._2)

}
