package com.x.p10Trait

/**
  * Date : 2016-04-07
  */
trait ConsolePrintLogger extends Logged {
  override def log(msg: String): Unit = {
    println(msg)
  }
}
