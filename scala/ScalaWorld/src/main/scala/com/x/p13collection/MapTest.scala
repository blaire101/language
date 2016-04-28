package com.x.p13collection

/**
  * Date : 2016-04-08
  */
object MapTest {

  def main(args: Array[String]) {
    val names = List("Peter", "Paul", "Mary")

    names.map(_.toUpperCase) // for (n <- names) yield n.toUpperCase

    // flatMap, 将所有的值串接在一起

    println(names.map(ulcase))

    println(names.flatMap(ulcase))

    // collect 方法用于 偏函数 (partial function) -- 那些并没有对所有可能的输入值进行定义的函数. 它产出被定义的所有参数的函数值的集合.

    "-3+4".collect { case '+' => 1; case '-' => -1 } // Vector(-1, 1)

    // 应用函数 到 各个元素 仅仅是为了 它的副作用而不关心函数值的话,可以用 foreach

    names.foreach(println)
  }

  def ulcase(s: String) = Vector(s.toUpperCase, s.toLowerCase)
}
