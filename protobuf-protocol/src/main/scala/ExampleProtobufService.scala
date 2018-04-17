package com.demo.protocol

import freestyle.rpc.protocol._

import shapeless.{:+:, CNil, Coproduct}

package protobuf {

  @message case class Request(value: String)

  //sealed trait ResponseType

  // @message case class UnknownFailure(value: String) extends ResponseType

  // @message case class InvalidInput(value: String) extends ResponseType

  @message case class Success(value: String, faillure: String) //extends ResponseType

  @message case class Response(value: Success)

  @service trait ExampleProtobufService[F[_]] {

    @rpc(Protobuf)
    def send(request: Request): F[Response]

  }
}