package com.x.p13collection

/**
  * Date : 2016-04-08
  */
object ListTest extends App {
  val digits = List(4, 2)

  println(digits.head)

  println(digits.tail)

  println(digits.tail.head)

  println(digits.tail.tail) // List() is Nil

  println((9 :: 4 :: 2 :: Nil).sum) // :: 右结合性, 9 :: (4 :: (2 :: Nil))

  // 链表求sum,你可以使用 递归 或者 模式匹配 case Nil => 0;case h :: t => h + sum(t)
}
