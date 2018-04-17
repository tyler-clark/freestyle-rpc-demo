package com.demo.server

import cats.effect.Sync
import cats.implicits._
import com.demo.protocol.protobuf._
import shapeless.syntax.inject._

class ExampleProtobufServiceHandler[F[_]](implicit S: Sync[F]) extends ExampleProtobufService[F] {
  override def send(request: Request): F[Response] = S.delay {
    // if (request.value === "") Response(UnknownFailure("Something Failed!"))
    // else if (request.value === "bad") Response(InvalidInput("Invalid Input!"))
    Response(Success(request.value, "fail"))
  }
}