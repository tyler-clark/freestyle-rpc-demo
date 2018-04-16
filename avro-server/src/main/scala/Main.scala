package com.example

import cats.effect.{Effect, IO}
import cats.implicits._
import fs2.{Stream, StreamApp}
import fs2.StreamApp.ExitCode
import freestyle.rpc.server.GrpcServer
import io.grpc.Server

object Main extends StreamApp[IO] {
  private[this] val logger = org.log4s.getLogger

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    streamRPCServer[IO]
  }

  import com.example.implicits._
  import freestyle.rpc.server.implicits._
  def streamRPCServer[F[_]: Effect]: Stream[F, ExitCode] =
    Stream.bracket(GrpcServer[F].start())(
      s => Stream.eval { awaitTermination(s) }.drain ++ Stream.emit(ExitCode.Success),
      s => shutdown(s)
    )

  def awaitTermination[F[_]](server: Server)(implicit E: Effect[F]): F[ExitCode] =
    E.delay(logger.info(s"RPC Server started on port ${server.getPort()}")) *>
      E.delay(server.awaitTermination()).handleErrorWith { e =>
        E.delay(logger.error(e)(s"rpc server failed with error $e"))
      } *> E.pure(ExitCode.Error)

  def shutdown[F[_]](server: Server)(implicit E: Effect[F]): F[Unit] =
    E.delay(logger.info("shutting down rpc server")) *> 
      E.delay(server.shutdown()).void.handleErrorWith { e =>
        E.delay(logger.error(e)(s"rpc server shutdown failed with error $e"))
      }
}