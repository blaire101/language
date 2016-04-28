package com.x.p10Trait

/**
  * Date : 2016-04-07
  */
trait Logger {
  def log(msg: String) // 这是个抽象方法, 你不需要声明为 abstract, 特质中未被实现的方法就是 抽象方法
}
