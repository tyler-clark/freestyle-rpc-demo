package com.demo.client

import cats.effect.IO
import com.demo.protocol.protobuf._
import freestyle.rpc.ChannelForAddress
import shapeless._
import shapeless.syntax.inject._

object ProtobufClientApp {

  implicit val S = monix.execution.Scheduler.Implicits.global

  val channelFor = ChannelForAddress("localhost", 8080)

  val exampleProtobufServiceClient: ExampleProtobufService.Client[IO] =
    ExampleProtobufService.client[IO](channelFor)

  def main(args: Array[String]): Unit = {

    val program = exampleProtobufServiceClient.send(Request(args(0)))

    val res = program.unsafeRunSync

    println(s"\n$res\n")

  }

}
