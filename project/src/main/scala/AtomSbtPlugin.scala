package arsenic.build

import sbt._

class AtomSbtPlugin extends AutoPlugin {

  object autoImport {
    val atomBuild = taskKey[Unit]("invoke npm and apm")
    val atomLink  = taskKey[Unit]("invoke npm link")
  }

  import autoImport._
  import scala.sys.process._
  override def projectSettings = Seq(
      atomBuild := {
      //npm install
      //apm rebuild
    }
    , atomLink := {
      //apm link --dev
    }
  )

  def generatePackageJson(name: String, main: File, version: String): String =
  s"""{
    |  "name": "$name",
    |  "main": "${escaped(main.absolutePath.toString)}",
    |  "version": "$version",
    |  "description": "A short description of your package",
    |  "keywords": [
    |  ],
    |  "activationCommands": {
    |    "atom-workspace": "$name:toggle"
    |  },
    |  "repository": "https://github.com/atom/my-fst-pkg",
    |  "license": "MIT",
    |  "engines": {
    |    "atom": ">=1.0.0 <2.0.0"
    |  },
    |  "dependencies": {
    |  }
    |}
  """.stripMargin

  def escaped(str: String): String = {
    import scala.reflect.runtime.universe._
    Literal(Constant(str)).toString
  }
}
