package com.x.p6object

/**
  * Date : 2016-04-05
  */
class Acc (val id: Int, initialBalance: Double) {
  private var balance = initialBalance
}

object Acc {
  private var lastNumber = 0

  private def newUniqueNumber() = {
    lastNumber += 1
    lastNumber
  }

  def apply(initialBalance: Double) =
    new Acc(newUniqueNumber(), initialBalance)
}