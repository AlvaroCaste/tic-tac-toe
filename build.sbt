import Dependencies._

ThisBuild / scalaVersion := "2.13.5"
ThisBuild / version := "0.1"
ThisBuild / organization := "dev.alvarocaste"
ThisBuild / organizationName := "AlvaroCaste"

lazy val root = (project in file("."))
  .settings(
    name := "tic-tac-toe",
    libraryDependencies ++= Seq(
          Libraries.cats,
          Libraries.catseffect,
          Libraries.newtype,
          Libraries.scalatest,
          Libraries.scalacheck,
          Libraries.scalacheckplus
        )
  )
