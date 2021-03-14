package dev.alvarocaste.tictactoe

import cats.implicits.{ catsSyntaxOptionId, catsSyntaxTuple2Semigroupal }
import dev.alvarocaste.tictactoe.domain.X._
import dev.alvarocaste.tictactoe.domain.Y._
import dev.alvarocaste.tictactoe.infra.{ Decoder, Encoder }
import dev.alvarocaste.tictactoe.infra.Decoder.DecoderClass
import dev.alvarocaste.tictactoe.infra.Encoder.EncoderClass

package object domain {

  sealed trait Player {
    def opposite: Player
  }

  object Player {
    case object Player1 extends Player { def opposite: Player = Player2 }
    case object Player2 extends Player { def opposite: Player = Player1 }

    implicit def encPlayer: Encoder[Player] = {
      case Player1 => "P1"
      case Player2 => "P2"
    }
  }

  sealed trait X

  object X {
    case object X0 extends X
    case object X1 extends X
    case object X2 extends X

    implicit def decX: Decoder[X] = {
      case "0" => X0.some
      case "1" => X1.some
      case "2" => X2.some
      case _   => None
    }

    implicit def encX: Encoder[X] = {
      case X0 => "0"
      case X1 => "1"
      case X2 => "2"
    }
  }

  sealed trait Y

  object Y {
    case object Y0 extends Y
    case object Y1 extends Y
    case object Y2 extends Y

    implicit def decY: Decoder[Y] = {
      case "0" => Y0.some
      case "1" => Y1.some
      case "2" => Y2.some
      case _   => None
    }

    implicit def encY: Encoder[Y] = {
      case Y0 => "0"
      case Y1 => "1"
      case Y2 => "2"
    }
  }

  case class Position(x: X, y: Y)

  object Position {
    implicit def decPosition: Decoder[Position] =
      (s: String) =>
        s.split(",") match {
          case Array(x, y) => (x.decode[X], y.decode[Y]).mapN(Position(_, _))
          case _           => None
        }

    implicit def encPosition: Encoder[Position] =
      (t: Position) => s"(${t.x.encode},${t.y.encode})"
  }

  case class Move(player: Player, position: Position)

  sealed trait Status

  object Status {
    case class GameState(game: Game) extends Status
    case class WrongPosition() extends Status
    case class FinishGame() extends Status
    case class Winner(player: Player) extends Status
  }

  type Game = Map[Position, Player]

  object Game {
    def empty: Game = Map.empty

    implicit def encGameStatus: Encoder[Game] =
      (t: Game) =>
        Array(
          Array(Position(X0, Y0), Position(X1, Y0), Position(X2, Y0)),
          Array(Position(X0, Y1), Position(X1, Y1), Position(X2, Y1)),
          Array(Position(X0, Y2), Position(X1, Y2), Position(X2, Y2))
        ).map(
            _.map(
              pos =>
                t.get(pos)
                  .map(p => s"  ${p.encode} ")
                  .getOrElse(pos.encode)
            ).mkString(",")
          )
          .mkString("\n")
  }
}
