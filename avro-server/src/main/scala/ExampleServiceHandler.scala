package com.example

import cats.effect.Sync
import cats.implicits._
import shapeless.syntax.inject._

class ExampleServiceHandler[F[_]](implicit S: Sync[F]) extends ExampleService[F] {
  override def send(request: Request): F[Response] = S.delay {
    if (request.value === "") Response(UnknownFailure("Something Failed!").inject[ResponseType])
    else if (request.value === "bad") Response(InvalidInput("Invalid Input!").inject[ResponseType])
    else Response(Success(request.value).inject[ResponseType])
  }
}