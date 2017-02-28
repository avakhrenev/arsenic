package io.arsenic.atom

import scala.scalajs.js
import js.annotation.ScalaJSDefined

/**
  *
  */
@ScalaJSDefined
abstract class AtomExtension(serialized: js.Object) extends js.Object {

  def serialize(): js.Object = js.Object()

  def destroy(): Unit = ()
}


/*
export default class MyFstPkgView {

  constructor(serializedState) {
    // Create root element
    this.element = document.createElement('div');
    this.element.classList.add('my-fst-pkg');

    // Create message element
    const message = document.createElement('div');
    message.textContent = 'The MyFstPkg package is Alive! It\'s ALIVE!';
    message.classList.add('message');
    this.element.appendChild(message);
  }

  // Returns an object that can be retrieved when package is activated
  serialize() {}

  // Tear down any state and detach
  destroy() {
    this.element.remove();
  }

  getElement() {
    return this.element;
  }

}
 */