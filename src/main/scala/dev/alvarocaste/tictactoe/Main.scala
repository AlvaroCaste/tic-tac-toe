package dev.alvarocaste.tictactoe

import cats.effect.{ ExitCode, IO, IOApp }
import dev.alvarocaste.tictactoe.domain.Player.Player1
import dev.alvarocaste.tictactoe.domain.{ Game, Move, Player, Position }
import dev.alvarocaste.tictactoe.domain.Game._
import dev.alvarocaste.tictactoe.domain.Status.{ FinishGame, GameState, Winner, WrongPosition }
import dev.alvarocaste.tictactoe.infra.Console
import dev.alvarocaste.tictactoe.infra.Console.ConsoleClass
import dev.alvarocaste.tictactoe.infra.Decoder.DecoderClass
import dev.alvarocaste.tictactoe.infra.Encoder.EncoderClass
import dev.alvarocaste.tictactoe.service.TicTacToe

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- "welcome Tic Tac Toe".printLine[IO]
      _ <- """Set your position "X,Y", being X or Y a number [0,2]""".printLine[IO]
      winner <- whoWin(Game.empty, Player1)
      _ <- winner.fold("no one won")(winner => s"winner is $winner").printLine[IO]
    } yield ExitCode.Success

  private def whoWin(game: Game, player: Player): IO[Option[Player]] =
    for {
      position <- playPlayer(player)
      status <- TicTacToe[IO].play(Move(player, position), game)
      winner <- status match {
                 case GameState(game) =>
                   game.encode.printLine[IO] *>
                       whoWin(game, player.opposite)
                 case Winner(winner) => IO.pure(Option(winner))
                 case FinishGame()   => IO.pure(None)
                 case WrongPosition() =>
                   "This position is already selected".printLine[IO] *>
                       game.encode.printLine[IO] *>
                       whoWin(game, player)
               }
    } yield winner

  private def playPlayer(player: Player): IO[Position] =
    for {
      _ <- s"Play $player".printLine[IO]
      posStr <- Console[IO].read()
      position <- posStr
                   .decode[Position]
                   .fold { "wrong input position, try again".printLine[IO] *> playPlayer(player) }(IO.pure)
    } yield position
}
