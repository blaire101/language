package com.x.mainTest

import com.x.p5class._

/**
  * Date : 2016-04-05
  */
object SampleDemo {
  // Single Object

  def main(args: Array[String]) = {

    /** Demo **/
    val demo = new Demo
    demo doStart "my first program with scala"

    /** chapter 5 **/

    println("chap 5")
    // 5.1  sample class
    val counter1 = new Counter
    val counter2 = new Counter
    counter2.increment()
    println(counter1.current)
    println(counter1.isLess(counter2))

    // 5.2 getter and setter
    val cat = new Cat
    cat.age = 9

    val fred = new Person1
    fred.age = 9
    println(fred.age)
    fred.age = 7
    println(fred.age)

    // 5.3 only getter
    val message = new Message
    println(message.timeStamp)


    // 5.6 auxiliary constructor
    val p1 = new Person2 // primary constructor
    var p2 = new Person2("Fred")
    var p3 = new Person2("Fred", 42)

  }
}
