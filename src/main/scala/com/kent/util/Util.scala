package com.kent.util

import java.util.Date

object Util {
  def nowTime:Long = (new Date()).getTime
  def nowDate:Date = new Date()
}