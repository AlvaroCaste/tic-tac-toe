package dev.alvarocaste.tictactoe.infra

import cats.effect.Sync

import scala.io.StdIn.readLine

trait Console[F[_]] {
  def printLine(s: String = ""): F[Unit]
  def read(): F[String]
}

object Console {
  def apply[F[_]](implicit F: Console[F]): Console[F] = F

  implicit def impl[F[_]: Sync]: Console[F] = new Console[F] {
    override def printLine(s: String): F[Unit] = Sync[F].delay(println(s))
    override def read(): F[String]             = Sync[F].delay(readLine())
  }

  implicit class ConsoleClass(value: String) {
    def printLine[F[_]: Sync]: F[Unit] = Console[F].printLine(value)
  }
}
