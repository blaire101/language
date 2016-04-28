package com.x.p6object

/**
  * Date : 2016-04-05
  */
object TrafficLightColor extends Enumeration {

  type TrafficLightColor = Value // 你可以 增加了一个类型别名 TrafficLightColor.TrafficLightColor 等价 TrafficLightColor.Value

  val Blue, Green = Value  // 缺省名称为 字段名

//  val Red = Value
//  val Yellow = Value
//  val Green = Value
  val Red = Value(10)

  val Yellow = Value
}
