package com.x.p13collection

/**
  * Date : 2016-04-08
  */
object LinkedListTest {

  def main(args: Array[String]) {
    val lst = scala.collection.mutable.LinkedList(1, -2, 7, -9)
    var cur = lst
    while (cur != Nil) {
      if (cur.elem < 0) {
        cur.elem = 0
      }
      cur = cur.next
    }

    println(lst) // LinkedList(1, 0, 7, 0)

    println(cur) // LinkedList()
  }
}
