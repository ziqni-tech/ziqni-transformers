package com.ziqni.transformers.domain
/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2023
  */
case class CreateEventAction(action: String, name: Option[String], unitOfMeasureKey: Option[String], metaData: Map[String, String] = Map.empty) {

}
