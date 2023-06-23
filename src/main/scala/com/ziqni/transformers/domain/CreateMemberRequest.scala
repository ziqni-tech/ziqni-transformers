package com.ziqni.transformers.domain

import scala.jdk.CollectionConverters.IterableHasAsJava

/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2023
  */
case class CreateMemberRequest(memberReferenceId: String, displayName: String, tags: Seq[String] = Seq.empty, customFields: Map[String,CustomFieldEntry[_<:Any]] = Map.empty, metadata: Map[String, String] = Map.empty) {

}
