package dev.alvarocaste.tictactoe.service

import cats.effect.IO
import dev.alvarocaste.tictactoe.domain.Player.{ Player1, Player2 }
import dev.alvarocaste.tictactoe.domain.Status.{ FinishGame, GameState, Winner, WrongPosition }
import dev.alvarocaste.tictactoe.domain.X._
import dev.alvarocaste.tictactoe.domain.Y._
import dev.alvarocaste.tictactoe.domain.{ Game, Move, Position }
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TicTacToeTest extends AnyFlatSpec with Matchers {

  "TicTacToe.play" should "continue" in {
    val game: Game = Map.empty
    TicTacToe[IO].play(Move(Player1, Position(X0, Y0)), game).unsafeRunSync() shouldBe GameState(
      Map(Position(X0, Y0) -> Player1)
    )
  }

  it should "win" in {
    val game: Game = Map(Position(X0, Y0) -> Player1, Position(X0, Y1) -> Player1)
    TicTacToe[IO].play(Move(Player1, Position(X0, Y2)), game).unsafeRunSync() shouldBe Winner(Player1)
  }

  it should "wrong position" in {
    val game: Game = Map(Position(X0, Y0) -> Player1)
    TicTacToe[IO].play(Move(Player2, Position(X0, Y0)), game).unsafeRunSync() shouldBe WrongPosition()
  }

  it should "finish" in {
    val game: Game = Map(
      Position(X0, Y0) -> Player1,
      Position(X1, Y0) -> Player2,
      Position(X2, Y0) -> Player1,
      Position(X0, Y1) -> Player2,
      Position(X1, Y1) -> Player1,
      Position(X2, Y1) -> Player1,
      Position(X0, Y2) -> Player1,
      Position(X1, Y2) -> Player2
    )
    TicTacToe[IO].play(Move(Player2, Position(X2, Y2)), game).unsafeRunSync() shouldBe FinishGame()
  }
}
