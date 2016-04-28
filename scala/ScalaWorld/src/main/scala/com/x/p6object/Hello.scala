package com.x.p6object

//import TrafficLightColor._
/**
  * Date : 2016-04-05
  *
  * 扩展 App 特性, 相当于一样可以完成 main 的功能, -Dscala.time
  */
object Hello extends App {
  if (args.length > 0) {
    println("Hello, " + args(0))
  }
  else {
    println("Hello, World2")
  }

  println(TrafficLightColor.Red)

  println(TrafficLightColor.Green)

  println(TrafficLightColor.Yellow)

  println(TrafficLightColor.Blue)

  println(TrafficLightColor.values)

  println(TrafficLightColor.withName("Blue"))

  println(TrafficLightColor(10))

}
