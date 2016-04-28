package com.x.p10Trait

import com.x.p6object.Account

/**
  * Date : 2016-04-07
  * desc : 带有具体实现的特质
  */
class SavingsAccount extends Account with ConsolePrintLogger {
  def withdraw(amount: Double): Unit = {
    if (amount > balance) {
      log("Insufficient funds")
    } else {
      balance -= amount
    }
  }
  // 注 : 让 Trait 存在具体行为存在一个 弊端. 当 Trait 改变时, 所有混入了该 Trait 的 Class 都必须 re compile
}
