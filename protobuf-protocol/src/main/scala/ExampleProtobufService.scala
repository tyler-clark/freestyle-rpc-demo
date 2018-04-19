package com.demo.protocol

import freestyle.rpc.protocol._

package protobuf {

  @message case class Request(value: String)

  @message case class Response(value: String)

  @service trait ExampleProtobufService[F[_]] {

    @rpc(Protobuf)
    def send(request: Request): F[Response]

  }
}