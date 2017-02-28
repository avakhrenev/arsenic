package io.arsenic

import io.arsenic.atom.AtomExtension

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, ScalaJSDefined}

/**
  *
  */
@ScalaJSDefined
@JSExport("MyFirstPackage")
class MyFirstPackage(state: js.Object) extends AtomExtension(state) {
  println("Oppa created!!!")
  override def serialize() = {
    println("Oppa serialized!!!")
    super.serialize()
  }

  override def destroy() = {
    println("Oppa destroyed!!!")
    super.destroy()
  }
}
