package com.x.p9File

import java.io.{File, FileInputStream, PrintWriter}

/**
  * Date : 2016-04-06
  */
object BinaryFileAndPrintWriteFile extends App {

  // 9.5 读取二进制文件 (使用Java类库)

  val file = new File("num.txt")

  val in = new FileInputStream(file)

  val bytes = new Array[Byte](file.length().toInt) // File readTo 字符数组

  in.read(bytes)

  for (c <- bytes) {
    println(c.toChar)
  }

  in.close()

  // 9.6 写入文本文件

  val out = new PrintWriter("numbers.txt")
  for (i <- 1 to 10) {
    out.println(i)
  }
  out.close()

}
