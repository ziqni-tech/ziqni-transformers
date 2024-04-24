package com.ziqni.transformers

case class ZiqniTransformerInfo(connectionId: String, connectionName: String, transformerId: String, customFields: Map[String, List[Object]], user: Option[String], secret: Option[String])
