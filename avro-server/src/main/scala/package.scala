package com

import shapeless._

package object example {

  type ResponseType = UnknownFailure :+: InvalidInput :+: Success :+: CNil
  
}