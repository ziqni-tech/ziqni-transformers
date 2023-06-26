/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2023
  */
package com.ziqni.transformers

case class ZiqniNotFoundException(val message: String) extends RuntimeException(message) {
  override def toString: EventbusAddress = message
}
