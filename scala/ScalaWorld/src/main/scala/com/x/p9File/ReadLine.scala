package com.x.p9File

import scala.io.Source

/**
  * Date : 2016-04-06
  */
object ReadLine extends App {

  val source = Source.fromFile("num.txt", "UTF-8")

  // 9.3 读取词法单元 和 数字
  val tokens = source.mkString.split("\\s+")

  val numbers = tokens.map(_.toDouble)

  for (w <- numbers) {
    println(w)
  }

  /* Output
  12.0
  34.0
  56.0
  78.0
  89.0
    */

  source.close()

  println("haow old are you ?")
  val age = readInt()
  println("age : " + age)

  // 9.4 从 URL 或 其他源 读取
  val source1 = Source.fromURL("http://52binge.com", "UTF-8") // URL 需要事先 知道字符集
  val source2 = Source.fromString("Hello,World")
  val source3 = Source.stdin

}

