package com.ziqni.transformers.domain

case class CreateMember(memberReferenceId: String, displayName: String, tags: Seq[String], customFields: Map[String,CustomFieldEntry[_<:Any]], metaData: Option[Map[String, String]] = None) {

}
