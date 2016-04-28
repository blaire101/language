package com.x.p9File

/**
  * Date : 2016-04-06
  */
object DirSerialize {

  // 9.7 访问目录, (Scala不是非常方便)

  // 9.8 序列化

  // 1. Java  序列化, public class Person implements java.io.Serializable
  // 2. Scala 序列化, @SerialVersionUID(42L) class Person extends Serializable

  /*

  val fred = new Person(...)
  import java.io._
  val out = new ObjectOutputStream(new FileOutputStream("/tmp/test.obj"))
  out.writeObject(fred)
  out.close()

  val in = new ObjectInputStream(new FileOInputStream("/tmp/test.obj"))
  val savedFred = in.readObject().asInstanceOf[person]

   */



  // Scala 集合类都是可序列化的, 因此 你可以把它们用做你的可序列化类的成员
  /*

  class Person extends Serializable {
    private val friends = new ArrayBuffer[Person] // ok--ArrayBuffer 是可序列化的

   */
}
