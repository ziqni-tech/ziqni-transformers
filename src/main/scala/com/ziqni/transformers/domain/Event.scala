package com.ziqni.transformers.domain

import org.joda.time.DateTime

case class Event(
                         memberRefId: String,
                         action: String,
                         batchId: Option[String],
                         entityRefId: String,
                         sourceValue: Double,
                         transactionTimestamp: DateTime,
                         tags: scala.Seq[String],
                         eventRefId: String,
                         memberId: Option[String],
                         customFields: Map[String,Any]
                       ) extends CustomFieldEntryImplicits {
  def asBasicEventModel: BasicEventModel = {

    val cf:Map[String,CustomFieldEntry[_<:Any]] = this.customFields.map(customFields => customFields._2 match {
      case in:String =>
        ( customFields._1, in )
      case in:Int =>
        ( customFields._1, in )
      case in:Double =>
        ( customFields._1,in )
      case in:Long =>
        ( customFields._1,in)
      case in:Boolean =>
        ( customFields._1, in)

      case in: List[String] =>
        (customFields._1, in)
      case in:List[Int] =>
        ( customFields._1, in)
      case in:List[Double] =>
        ( customFields._1, in)
      case in:List[Long] =>
        ( customFields._1, in)
      case _ =>
        ( customFields._1,CustomFieldEntryText(""))
    })

    BasicEventModel(
      memberId = memberId,
      memberRefId = memberRefId,
      entityRefId = entityRefId,
      eventRefId = entityRefId,
      batchId =  batchId,
      action = action,
      sourceValue = sourceValue,
      transactionTimestamp = transactionTimestamp,
      tags = tags,
      customFields = cf
    )
  }
}