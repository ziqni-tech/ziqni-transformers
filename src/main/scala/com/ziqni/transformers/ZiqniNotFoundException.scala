/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2023
  */
package com.ziqni.transformers

case class ZiqniNotFoundException(objectType: String, id: String, isReference:Boolean) extends RuntimeException(s"Not found: objectType=$objectType, isReference=$isReference, id=$id") {
  override def toString: String = super.getMessage
}
