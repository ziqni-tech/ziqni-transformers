package com.ziqni.transformers.domain
/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2023
  */
case class CreateMember(memberReferenceId: String, displayName: String, tags: Seq[String], customFields: Map[String,CustomFieldEntry[_<:Any]], metaData: Option[Map[String, String]] = None) {

}
