package com.example

import cats.effect.IO
import java.util.UUID
import scala.concurrent.duration._
import shapeless._
import shapeless.syntax.inject._


object AvroClientApp {
  import clients._

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
