package com.ziqni.transformers.domain
/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2023
  */
case class CreateEventActionRequest(action: String, name: Option[String], unitOfMeasureKey: Option[String], metadata: Map[String, String] = Map.empty) {

}
