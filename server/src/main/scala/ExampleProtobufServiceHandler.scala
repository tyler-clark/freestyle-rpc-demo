package com.demo.server

import cats.Applicative
import com.demo.protocol.protobuf._

class ExampleProtobufServiceHandler[F[_]](implicit A: Applicative[F]) extends ExampleProtobufService[F] {
  override def send(request: Request): F[Response] = A.pure { Response(request.value) }
}