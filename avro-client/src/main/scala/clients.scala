package com.example

import cats.effect.IO
import freestyle.rpc.ChannelForAddress

object clients {

  implicit val S = monix.execution.Scheduler.Implicits.global

  val channelFor = ChannelForAddress("localhost", 8080)

  val exampleServiceClient: ExampleService.Client[IO] =
    ExampleService.client[IO](channelFor)

}