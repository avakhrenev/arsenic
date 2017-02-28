inThisBuild(
  Seq(
      scalaVersion := "2.12.1"
    , version := "0.0.1"
  ))

lazy val arsenic = (project in file(".")
    enablePlugins AtomSbtPlugin
    settings (
      name := "arsenic"
    ))
