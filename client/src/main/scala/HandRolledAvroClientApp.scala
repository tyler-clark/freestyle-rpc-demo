package com.demo.client

import io.grpc.{ManagedChannelBuilder, MethodDescriptor}
import io.grpc.MethodDescriptor.Marshaller
import io.grpc.stub.ClientCalls

object marshallers {

  import com.sksamuel.avro4s._
  import org.apache.avro.file.DataFileStream
  import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
  import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream}

  implicit def avroMarshallers[A: ToRecord](
      implicit schemaFor: SchemaFor[A],
      fromRecord: FromRecord[A]): Marshaller[A] = new Marshaller[A] {

    override def parse(stream: InputStream): A = {
      val dfs = new DataFileStream(stream, new GenericDatumReader[GenericRecord](schemaFor()))
      fromRecord(dfs.next())
    }

    override def stream(value: A): InputStream = {
      val baos: ByteArrayOutputStream     = new ByteArrayOutputStream()
      val output: AvroDataOutputStream[A] = AvroOutputStream.data[A](baos)
      output.write(value)
      output.close()

      new ByteArrayInputStream(baos.toByteArray)
    }
  }
}

object HandRolledAvroClientApp {

  import marshallers._
  import shapeless._

  case class Request(value: String)
  case class UnknownFailure(value: String)
  case class InvalidInput(value: String)
  case class Success(value: String)
  case class Response(value: UnknownFailure :+: InvalidInput :+: Success :+: CNil)

  object ResponsePoly extends Poly1 {
    implicit val a = at[UnknownFailure](u => "Something unknown happened!")
    implicit val b = at[InvalidInput](u => "The input you provided is invalid!")
    implicit val c = at[Success](s => s.value)
  }

  def methodDescriptor[A, B](serviceName: String, methodName: String)(implicit aMarsh: Marshaller[A], bMarsh: Marshaller[B]): MethodDescriptor[A, B] = {
    MethodDescriptor
      .newBuilder(aMarsh, bMarsh)
      .setType(MethodDescriptor.MethodType.UNARY)
      .setFullMethodName(MethodDescriptor.generateFullMethodName(serviceName, methodName))
      .build()
  }

  val channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build()

  def main(args: Array[String]): Unit = {

    val call = channel.newCall(methodDescriptor[Request, Response]("ExampleAvroService", "send"), io.grpc.CallOptions.DEFAULT)

    val program = ClientCalls.blockingUnaryCall(call, Request(args(0)))

    val result = program.value.fold(ResponsePoly)

    println(s"\n$result\n")

  }

}


