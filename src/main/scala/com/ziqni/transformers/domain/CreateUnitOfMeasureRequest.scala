package com.ziqni.transformers.domain
/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2023
  */
class CreateUnitOfMeasureRequest(val key: String, val name: String, val multiplier: Double, val isoCode: Option[String] = None, val unitOfMeasureType: Option[String] = None, val customFields: Map[String,CustomFieldEntry[_<:Any]] = Map.empty) {

}
