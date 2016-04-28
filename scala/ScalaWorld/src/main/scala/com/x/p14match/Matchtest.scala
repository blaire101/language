package com.x.p14match

/**
  * Date : 2016-04-12
  */
object Matchtest {

  def main(args: Array[String]) {
    var sign = -20
    val ch: Char = '-'

    sign = ch match {
      case '+' => 1
      case '-' => -1
      case _ => 0
    }
    println(sign)
  }
}
