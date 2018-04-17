package com.demo

import com.demo.protocol.avro
import com.demo.protocol.protobuf
import shapeless._

package object server {

  type AvroResponseType = avro.UnknownFailure :+: avro.InvalidInput :+: avro.Success :+: CNil

  type ProtobufResponseType = protobuf.UnknownFailure :+: protobuf.InvalidInput :+: protobuf.Success :+: CNil
  
}