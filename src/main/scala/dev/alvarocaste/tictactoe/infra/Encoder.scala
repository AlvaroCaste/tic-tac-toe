package dev.alvarocaste.tictactoe.infra

trait Encoder[T] {
  def encode(t: T): String
}

object Encoder {
  def apply[T](implicit T: Encoder[T]): Encoder[T] = T

  implicit class EncoderClass[T: Encoder](value: T) {
    def encode: String = Encoder[T].encode(value)
  }
}
