package com.x.p6object

/**
  * Date : 2016-04-05
  * Desc : 一个 object 可以扩展类以及一个或多个特质 , 结果是 同时 拥有在对象定义中给出的所有特性
  *
  * DoNothing 对象可以被所有需要这个缺省行为的地方共用
  */
object DoNothingAction extends UndoableAction("Do nothing"){
  override def undo() {}
  override def redo() {}

  def main(args: Array[String]): Unit = {
    val actions = Map("open"-> DoNothingAction, "save"->DoNothingAction)

    println("open and save function is not implement.")
  }
}
