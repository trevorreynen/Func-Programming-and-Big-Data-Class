// "FunctionalBigDataV3" changed to "FunctionalProgramming".
// I Guess name doesn't really matter?
// "FunctionalProgramming" changed to "Func-Programming-and-Big-Data-Class"
name := "Func-Programming-and-Big-Data-Class"
version := "1.0" // 1.0 changed to 0.1.0-SNAPSHOT
scalaVersion := "2.12.8" // 2.12.12 changed to 2.12.8
libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "3.0.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.0.0"

/*
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.8"

lazy val root = (project in file("."))
  .settings(
    name := "FunctionalProgramming"
  )
*/
