package com.ziqni.transformers.domain
/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2023
  */
case class CreateProductRequest(productReferenceId: String, displayName: String, tags: Seq[String], productType: String, defaultAdjustmentFactor: Double, customFields: Map[String,CustomFieldEntry[_<:Any]] = Map.empty, metadata: Map[String, String] = Map.empty) {

}
