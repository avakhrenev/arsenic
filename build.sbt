scalaVersion in ThisBuild := "2.12.1"

lazy val arsenic = (project in file(".")
  enablePlugins ScalaJSPlugin 
  settings(
    name := "arsenic"
  )
)

