package com.demo.server

import cats.effect.{Effect, Sync}
import com.demo.protocol.avro.ExampleAvroService
import com.demo.protocol.protobuf.ExampleProtobufService
import freestyle.rpc.server.{AddService, GrpcConfig, ServerW}

trait ServerImplicits {
  implicit val S = monix.execution.Scheduler.Implicits.global

  implicit def exampleAvroService[F[_]: Sync]: ExampleAvroService[F] = new ExampleAvroServiceHandler

  implicit def exampleProtobufService[F[_]: Sync]: ExampleProtobufService[F] = new ExampleProtobufServiceHandler

  def serviceDefs[F[_]: Effect] = List(ExampleAvroService.bindService[F], ExampleProtobufService.bindService[F])

  def grpcConfigs[F[_]: Effect]: List[GrpcConfig] = serviceDefs[F].map(AddService)

  implicit def serverW[F[_]: Effect]: ServerW = ServerW.default(8080, grpcConfigs[F])
}

object implicits extends ServerImplicits