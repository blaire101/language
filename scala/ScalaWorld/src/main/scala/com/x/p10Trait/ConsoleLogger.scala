package com.x.p10Trait

/**
  * Date : 2016-04-07
  * 所有的 Java 接口 都可以 作为 Scala rait 使用
  */
class ConsoleLogger extends Logger with Cloneable with Serializable {
  // use extends
  def log(msg: String): Unit = { // 重写 Trait 的 抽象方法 不需要给出 override 关键字
    println(msg)
  }
  // 和 Java 一样, Scala 类 只能有一个 超类, 但 可以有 任意数量的 Trait

}
