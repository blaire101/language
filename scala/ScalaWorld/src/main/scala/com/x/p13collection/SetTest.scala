package com.x.p13collection

/**
  * Date : 2016-04-08
  */
object SetTest {

  def main(args: Array[String]) {

    val s = Set(1, 2, 3, 4) + 5 + 6

    println(s)

    val weekdays = scala.collection.mutable.LinkedHashSet("Mo", "Tu", "We", "Th", "Fr")

    println(weekdays)

    val s2 = scala.collection.immutable.SortedSet(1, 3, 5, 2)

    println(s2)

    // 注: 可变的已排序集, java.util.TreeSet
  }

}
