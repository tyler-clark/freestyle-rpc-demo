package com.demo.server

import cats.effect.Sync
import cats.implicits._
import com.demo.protocol.avro._
import shapeless.syntax.inject._

class ExampleAvroServiceHandler[F[_]](implicit S: Sync[F]) extends ExampleAvroService[F] {
  override def send(request: Request): F[Response] = S.delay {
    if (request.value === "") Response(UnknownFailure("Something Failed!").inject[AvroResponseType])
    else if (request.value === "bad") Response(InvalidInput("Invalid Input!").inject[AvroResponseType])
    else Response(Success(request.value).inject[AvroResponseType])
  }
}