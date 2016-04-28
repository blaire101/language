package com.x.p10Trait

import com.x.p6object.Account

/**
  * Date : 2016-04-07
  */
class SavingsAcc extends Account with Logged {
  def withdraw(amount: Double): Unit = {
    if (amount > balance) {
      log("Insufficient funds")
    } else {
      balance -= amount
    }
  }
}
// 看上去,毫无意义, 其实不然,你可以在 构造对象 的时候 "混入" 一个更好的 日志 记录器

object SavingsAcc extends App {
  val acct1 = new SavingsAcc with ConsolePrintLogger
//  val acct2 = new SavingsAcc with FileLogger
  acct1.withdraw(100)
}