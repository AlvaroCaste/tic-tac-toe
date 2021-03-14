
ThisBuild / scalaVersion := "2.13.5"
ThisBuild / version := "0.1"
ThisBuild / organization := "dev.alvarocaste"
ThisBuild / organizationName := "AlvaroCaste"

lazy val root = (project in file("."))
  .settings(
    name := "tic-tac-toe"
  )