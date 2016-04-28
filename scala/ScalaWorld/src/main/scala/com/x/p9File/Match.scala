package com.x.p9File

/**
  * Date : 2016-04-06
  */
object Match extends App {
  val numPattern = "[0-9]".r
  val wsnumwsPattern = """\s+[0-9]+\s""".r

  for (matchString <- numPattern.findAllIn("99 bottles, 98 bottles")) {
    println(matchString)
  }

  for (matchString <- wsnumwsPattern.findAllIn("99 bottles, 998 9bottles")) {
    println("B" + matchString + "B")
  }

  // 9.10 正则 表达式 组
  val numitemPattern = "([0-9]+) ([a-z]+)".r
  val numitemPattern(num, item) = "99 bottles"
  for (numitemPattern(num, item) <- numitemPattern.findAllIn("99 bottles, 998 9bottles")) {
    println(num + " " + item)
  }

}
