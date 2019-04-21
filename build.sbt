name := """2ndYearProject"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.8"

javacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-parameters",
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-Werror"
)

crossScalaVersions := Seq("2.11.12", "2.12.7")

libraryDependencies += guice

// Test Database H2
libraryDependencies += "com.h2database" % "h2" % "1.4.197"

// Image scaling
// libraryDependencies += "org.imgscalr" % "imgscalr-lib" % "4.2"

// Testing libraries for dealing with CompletionStage...
//libraryDependencies += "org.assertj" % "assertj-core" % "3.11.1" % Test
//libraryDependencies += "org.awaitility" % "awaitility" % "3.1.3" % Test


// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// Include evolutions
libraryDependencies ++= Seq(evolutions, jdbc)


