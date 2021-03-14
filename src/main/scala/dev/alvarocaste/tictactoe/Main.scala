package dev.alvarocaste.tictactoe

import cats.effect.{ ExitCode, IO, IOApp }
import dev.alvarocaste.tictactoe.domain.Player.Player1
import dev.alvarocaste.tictactoe.domain.{ Game, Move, Player, Position }
import dev.alvarocaste.tictactoe.domain.Game._
import dev.alvarocaste.tictactoe.domain.Status.{ FinishGame, GameState, Winner, WrongPosition }
import dev.alvarocaste.tictactoe.infra.Console
import dev.alvarocaste.tictactoe.infra.Decoder.DecoderClass
import dev.alvarocaste.tictactoe.infra.Encoder.EncoderClass
import dev.alvarocaste.tictactoe.service.TicTacToe

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- Console[IO].printLine("welcome Tic Tac Toe")
      _ <- Console[IO].printLine("""Set your position "X,Y", being X or Y a number [0,2]""")
      winner <- whoWin(Game.empty, Player1)
      _ <- winner.fold(Console[IO].printLine("no one won"))(winner => Console[IO].printLine(s"winner is $winner"))
    } yield ExitCode.Success

  private def whoWin(game: Game, player: Player): IO[Option[Player]] =
    for {
      position <- playPlayer(player)
      status <- TicTacToe[IO].play(Move(player, position), game)
      winner <- status match {
                 case GameState(game) =>
                   Console[IO].printLine(game.encode) *>
                       whoWin(game, player.opposite)
                 case Winner(winner) => IO.pure(Option(winner))
                 case FinishGame()   => IO.pure(None)
                 case WrongPosition() =>
                   Console[IO].printLine("This position is already selected") *>
                       Console[IO].printLine(game.encode) *>
                       whoWin(game, player)
               }
    } yield winner

  private def playPlayer(player: Player): IO[Position] =
    for {
      _ <- Console[IO].printLine(s"Play $player")
      posStr <- Console[IO].read()
      position <- posStr
                   .decode[Position]
                   .fold {
                     Console[IO].printLine("wrong input position, try again") *> playPlayer(player)
                   }(IO.pure)
    } yield position
}
