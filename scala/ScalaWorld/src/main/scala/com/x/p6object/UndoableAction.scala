package com.x.p6object

/**
  * Date : 2016-04-05
  */
abstract class UndoableAction(val desc: String) {
  def undo(): Unit
  def redo(): Unit
}
