package dev.alvarocaste.tictactoe.service

import cats.effect.Sync
import dev.alvarocaste.tictactoe.domain.Status.{ FinishGame, GameState, Winner, WrongPosition }
import dev.alvarocaste.tictactoe.domain.X._
import dev.alvarocaste.tictactoe.domain.Y._
import dev.alvarocaste.tictactoe.domain.{ Game, Move, Player, Position, Status }

trait TicTacToe[F[_]] {
  def play(move: Move, status: Game): F[Status]
}

object TicTacToe {
  def apply[F[_]](implicit F: TicTacToe[F]): TicTacToe[F] = F

  implicit def impl[F[_]: Sync]: TicTacToe[F] = new TicTacToe[F] {

    def play(move: Move, status: Game): F[Status] =
      Sync[F].pure(
        status
          .get(move.position)
          .fold(isFinish(status + (move.position -> move.player), move.player))(_ => WrongPosition())
      )

    def isFinish(game: Game, player: Player): Status =
      WinnerPositions
        .collectFirst {
          case winnerPos if winnerPos.forall(p => game.get(p).contains(player)) => Winner(player)
        }
        .getOrElse { if (game.size == 9) FinishGame() else GameState(game) }

    private val WinnerPositions: Set[Set[Position]] = Set(
      // Rows
      Set(Position(X0, Y0), Position(X1, Y0), Position(X2, Y0)),
      Set(Position(X0, Y1), Position(X1, Y1), Position(X2, Y1)),
      Set(Position(X0, Y2), Position(X1, Y2), Position(X2, Y2)),
      // Columns
      Set(Position(X0, Y0), Position(X0, Y1), Position(X0, Y2)),
      Set(Position(X1, Y0), Position(X1, Y1), Position(X1, Y2)),
      Set(Position(X2, Y0), Position(X2, Y1), Position(X2, Y2)),
      // Diagonal
      Set(Position(X0, Y0), Position(X1, Y1), Position(X2, Y2)),
      Set(Position(X0, Y2), Position(X1, Y1), Position(X2, Y0))
    )
  }
}
