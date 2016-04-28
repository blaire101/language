package com.x.p6object

/**
  * Date : 2016-04-05
  */
class Account {
  val id = Account.newUniqueNumber() // Scala getter
  var balance = 0.0          // Scala getter and setter
  def deposit(amount: Double): Unit = {
    balance += amount
  }
}
object Account { // 伴生对象
  private var lastNumber = 0

  private def newUniqueNumber() = {
    lastNumber += 1
    lastNumber
  }
}

/**
  * Account.newUniqueNumber() 非 newUniqueNumber()
  *
  * 它们必须在同一个源文件中
  */
