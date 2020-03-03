name := """GestorGGestor"""
organization := "RecetasRR"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .settings(
    name := "GestorGGestor",
    version := "2.7.x",
    scalaVersion := "2.13.0",
    libraryDependencies ++= Seq(
      ehcache,
      jdbc,
      evolutions,
      guice,
      javaJpa,
      "com.h2database" % "h2" % "1.4.199",
      "org.hibernate" % "hibernate-core" % "5.4.2.Final",
      "io.dropwizard.metrics" % "metrics-core" % "3.2.6",
      "com.palominolabs.http" % "url-builder" % "1.1.0",
      "net.jodah" % "failsafe" % "1.0.5",
      "org.avaje" % "ebean" % "2.7.3"

    ),
    PlayKeys.externalizeResources := false,
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v")),
    javacOptions ++= Seq(
      "-Xlint:unchecked",
      "-Xlint:deprecation"
    )
  )

val gatlingVersion = "3.1.3"
lazy val gatling = (project in file("gatling"))
  .enablePlugins(GatlingPlugin)
  .settings(
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % Test,
      "io.gatling" % "gatling-test-framework" % gatlingVersion % Test
    )


    
  )

def excludeJPAPersistence(module: ModuleID): ModuleID =
  module.excludeAll(ExclusionRule("javax.persistence","persistence-api"))

libraryDependencies ~= (_.map(excludeJPAPersistence))






