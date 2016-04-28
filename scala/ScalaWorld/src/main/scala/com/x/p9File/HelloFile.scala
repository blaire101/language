package com.x.p9File

import java.io.PrintWriter
import java.io.File

import scala.io.Source


/**
  * Date : 2016-04-06
  */
object HelloFile extends App {

  val writer = new PrintWriter(new File("test.txt")) // base dir will generate test.txt

  writer.write("Hello Scala In test.txt")

  writer.close()

  // 9.0 console read
  print("Please enter your input : ")

  val line = Console.readLine

  println("Thanks, you just typed: " + line)

  // 9.1 Read File content
  println("Following is the content read:")

  Source.fromFile("test.txt").foreach {
    print
  }

  // mkString / Array
  val source = Source.fromFile("test.txt")

  val contents = source.mkString
//
//  val arr = source.getLines().toArray
//
  println("\n\ncontents : " + contents + "\n")
//
//  println("\n\narr : " + arr)
  source.close()

  // 9.2 读取字符

  val source2 = Source.fromFile("test.txt")

  val iterator = source2.buffered

  while (iterator.hasNext) {
    if (iterator.head != 'e') {
      print(iterator.next())
    } else {
      iterator.next()
    }
  }

  source2.close()

}
