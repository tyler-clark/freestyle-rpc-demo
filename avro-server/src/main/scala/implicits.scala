package com.example

import cats.effect.{Effect, Sync}
import freestyle.rpc.server.{AddService, GrpcConfig, ServerW}
import freestyle.rpc.server.implicits._

trait ServerImplicits {
  implicit val S = monix.execution.Scheduler.Implicits.global

  implicit def exampleService[F[_]: Sync]: ExampleService[F] = new ExampleServiceHandler

  def serviceDefs[F[_]: Effect] = List(ExampleService.bindService[F])

  def grpcConfigs[F[_]: Effect]: List[GrpcConfig] = serviceDefs[F].map(AddService)

  implicit def serverW[F[_]: Effect]: ServerW = ServerW.default(8080, grpcConfigs[F])
}

object implicits extends ServerImplicits