import org.openurp.parent.Dependencies.*
import org.openurp.parent.Settings.*

ThisBuild / organization := "org.openurp.prac.paper"
ThisBuild / version := "0.0.3"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/openurp/prac-paper"),
    "scm:git@github.com:openurp/prac-paper.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "chaostone",
    name = "Tihua Duan",
    email = "duantihua@gmail.com",
    url = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "The OpenURP Practice Paper Webapp"
ThisBuild / homepage := Some(url("http://openurp.github.io/prac-paper/index.html"))
ThisBuild / resolvers += Resolver.mavenLocal

val apiVer = "0.48.2"
val starterVer = "0.4.8"
val baseVer = "0.4.63"
val openurp_base_api = "org.openurp.base" % "openurp-base-api" % apiVer
val openurp_stater_web = "org.openurp.starter" % "openurp-starter-web" % starterVer
val openurp_base_tag = "org.openurp.base" % "openurp-base-tag" % baseVer

lazy val root = (project in file("."))
  .enablePlugins(WarPlugin, UndertowPlugin, TomcatPlugin)
  .settings(
    name := "openurp-prac-paper-webapp",
    common,
    libraryDependencies ++= Seq(openurp_base_api, openurp_base_tag, openurp_stater_web),
    libraryDependencies ++= Seq(logback_classic)
  )
