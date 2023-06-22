package com.ziqni.transformers.domain

case class CreateEventAction(action: String, name: Option[String], metaData: Option[Map[String, String]], unitOfMeasureKey: Option[String]) {

}
