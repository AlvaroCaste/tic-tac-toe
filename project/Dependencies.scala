import sbt._

object Dependencies {

  object V {
    val cats       = "2.4.2"
    val catseffect = "2.3.3"
    val newtype    = "0.4.4"

    val scalatest     = "3.2.6"
    val scalacheck    = "1.15.3"
    val scalatestplus = "3.2.6.0"
  }

  object Libraries {
    val cats       = "org.typelevel" %% "cats-core"   % V.cats
    val catseffect = "org.typelevel" %% "cats-effect" % V.catseffect
    val newtype    = "io.estatico"   %% "newtype"     % V.newtype

    val scalatest      = "org.scalatest"     %% "scalatest"       % V.scalatest
    val scalacheck     = "org.scalacheck"    %% "scalacheck"      % V.scalacheck
    val scalacheckplus = "org.scalatestplus" %% "scalacheck-1-15" % V.scalatestplus
  }
}
