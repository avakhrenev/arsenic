package arsenic.build

import java.nio.charset.StandardCharsets
import java.util.Locale

import org.scalajs.sbtplugin.ScalaJSPlugin
import sbt._
import Keys._

object AtomSbtPlugin extends AutoPlugin {

  override def requires = ScalaJSPlugin

  object autoImport {
    val atomTarget = settingKey[File]("base directory for atom module")
    val atomBuild  = taskKey[Unit]("invoke npm and apm")
    val atomLink   = taskKey[Unit]("invoke npm link")
    val atomStart  = taskKey[Unit]("start atom in dev mode")
    val atomUnlink = taskKey[Unit]("unlinks atom package")
  }
  import ScalaJSPlugin.autoImport._
  import autoImport._
  override def projectSettings =
    Seq(
        scalaJSModuleKind := ModuleKind.CommonJSModule
    ) ++ inConfig(Compile)(
      Seq(
          atomTarget := crossTarget.value / "atom"
        , atomBuild := {
          val output = fastOptJS.value
          val wd     = atomTarget.value
          IO.createDirectory(wd)
          IO.copyFile(output.data, wd / "lib" / output.data.name)
          IO.write(wd / "package.json",
                   generatePackageJson(name.value, s"lib/${output.data.name}", version.value),
                   StandardCharsets.UTF_8)
          exec(wd, "npm", "install") !! streams.value.log
          exec(wd, "apm", "rebuild") !! streams.value.log
        }
        , atomLink := {
          val _ = atomBuild.value
          exec(atomTarget.value, "apm", "link", "--dev") !! streams.value.log
        }
        , atomStart := {
          val _ = atomLink.value
          exec(atomTarget.value, "atom", "--dev", ".") !! streams.value.log
        }
        , atomUnlink := {
          exec(atomTarget.value, "apm", "unlink", "--dev") !! streams.value.log
        }
      ))

  def generatePackageJson(name: String, main: String, version: String): String =
    s"""{
       |  "name": "$name",
       |  "main": "$main",
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

  // Copy-pasted from sbt.Resolvers to make Windows search for npm.cmd, npm.bat etc within PATH
  // Surprisingly, there is no ready to use code available
  def exec(cwd: File, command: String*): ProcessBuilder = Process(
      if (onWindows) "cmd" +: "/c" +: command
    else command
    , Some(cwd)
  )

  private lazy val onWindows = {
    val os       = System.getenv("OSTYPE")
    val isCygwin = (os != null) && os.toLowerCase(Locale.ENGLISH).contains("cygwin")
    val isWindows =
      System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).contains("windows")
    isWindows && !isCygwin
  }
}
