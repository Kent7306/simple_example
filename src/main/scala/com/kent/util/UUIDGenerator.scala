package com.kent.util

import java.util.UUID

object UUIDGenerator {
  def produce8UUID: String = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8)
}