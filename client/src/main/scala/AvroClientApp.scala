package com.demo.client

import cats.effect.IO
import com.demo.protocol.avro._
import freestyle.rpc.ChannelForAddress
import shapeless._
import shapeless.syntax.inject._

object AvroClientApp {

  implicit val S = monix.execution.Scheduler.Implicits.global

  val channelFor = ChannelForAddress("localhost", 8080)

  val exampleServiceClient: ExampleAvroService.Client[IO] =
    ExampleAvroService.client[IO](channelFor)

  def main(args: Array[String]): Unit = {

    object ResponsePoly extends Poly1 {
      implicit val a = at[UnknownFailure](u => "Something unknown happened!")
      implicit val b = at[InvalidInput](u => "The input you provided is invalid!")
      implicit val c = at[Success](s => s.value)
    }

    val program = exampleServiceClient.send(Request(args(0)))

    val result = program.unsafeRunSync.value.fold(ResponsePoly)

    println(s"\n$result\n")

  }

}
