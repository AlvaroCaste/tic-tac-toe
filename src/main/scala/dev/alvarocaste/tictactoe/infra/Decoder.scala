package dev.alvarocaste.tictactoe.infra

trait Decoder[T] {
  def decode(s: String): Option[T]
}

object Decoder {
  def apply[T](implicit T: Decoder[T]): Decoder[T] = T

  implicit class DecoderClass(value: String) {
    def decode[T: Decoder]: Option[T] = Decoder[T].decode(value)
  }
}
