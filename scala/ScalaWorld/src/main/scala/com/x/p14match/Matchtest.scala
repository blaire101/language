package com.x.p14match

import java.lang.Character

/**
  * Date : 2016-04-12
  */
object Matchtest {

  def main(args: Array[String]) {

    /** 14.1 best switch  **/

    var sign = -20
    var ch: Char = '-'

    sign = ch match {
      case '+' => 1
      case '-' => -1
      case _ => 0
    }
    println("sign : " + sign)

    // scala 模式匹配 与 switch 是不同的
    // you can use anytype in match. not only number

    /** 14.2  守卫  **/

    ch = '8'
    var digit = 0

    ch match {
      case '+' => 1
      case '-' => -1
      case _ if Character.isDigit(ch) => digit = Character.digit(ch, 10) // 0 ~ 9

      case _ => sign = 2
    }
    println("sign : " + sign)
    println("digit : " + digit)

    /** 14.3  模式中的变量  **/

    // match中 变量 必须以小写字母开头, 否则 需要 `varname`

    /** 14.4 类型模式 **/

    var obj: Any = "123"

    var mk = obj match {
      case x: Int => x
      case s: String => Integer.parseInt(s)
      case _: BigInt => Int.MaxValue
      case _ => 0
    }

    println("type_k : " + mk)

    /** 14.5 匹配数据, 列表, 元组 **/

    // array

    val numArr = new Array[Int](5)
    numArr(0) = 1

    for (i <- 0 until numArr.length)
      println(i + ": " + numArr(i))

    val na_res = numArr match {
      case Array(0) => "0"            // inclue 0 array, only one elem, is 0
      case Array(x, y) => x + " " + y // only have 2 element array
      case Array(0, _*) => "0 ..."    // any by 0 begin array
      case _ => "something else"
    }

    println("na_res : " + na_res)

    // list

    val lst = List(0, 8, 9)

    val lstRes = lst match {
      case 0 :: Nil => "0"
      case x :: y :: Nil => x + " and " + y
      case 0 :: tail => "0 ..."
      case _ => "something else"
    }

    println("lstRes : " + lstRes)

    // tuple

    val pair = (2, 3.14, "Hello")

    val pairRes = pair match {
      case (0, _, _) => "0 ..."
      case (y, z, x) => z + 5
      case _ => "neither is 0"
    }

    println("pairRes : " + pairRes)

    /** 14.6 提取器 **/

    /** 14.7 变量声明中的模式 **/

    val (x, y) = (1, 2)
    println(x + y)
    val (q, r) = BigInt(10) /% 3

    val Array(first, second, _*) = numArr


    println("first : " + first)


    /** 14.8 for 表达式中的模式 **/

    import scala.collection.JavaConversions.propertiesAsScalaMap // 将 Java 的 Properties 转换成 Scala 映射
    for ((k, v) <- System.getProperties() if v == "")
      println("key : " + k)

    /** 14.9 样例类 **/


  }
}
