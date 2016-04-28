package com.x.p6object

/**
  * Date : 2016-04-05
  */
object Accounts {
  private var lastNumber = 0
  def newUniqeNumber() = {
    lastNumber += 1;
    lastNumber
  }

}

/**
  * Scala 中 Object 可作为存放 工具函数 或 常量, 高效地共享 单个不可变实例
  */
