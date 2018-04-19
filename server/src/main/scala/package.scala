package com.demo

import com.demo.protocol.avro._
import shapeless._

package object server {

  type AvroResponseType = UnknownFailure :+: InvalidInput :+: Success :+: CNil
  
}