package com.ziqni.transformers.defaults

import com.ziqni.transformers.ZiqniContext
import com.ziqni.transformers.domain.{BasicEventModel, CustomFieldEntry, CustomFieldEntryImplicits, CustomFieldEntryNumber, CustomFieldEntryNumberArray, CustomFieldEntryText, CustomFieldEntryTextArray}
import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.jackson.parseJson

object Event {

  def fromByteArray(message: Array[Byte]): Event = {
    implicit val formats: DefaultFormats.type = DefaultFormats
    val messageAsString = ZiqniContext.convertByteArrayToString(message)
    parseJson(messageAsString).extract[Event]
  }
}

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

    def handleSeq(key:String, list:Seq[_]): (String, CustomFieldEntry[_<:Any]) = {
      list.headOption.map {
        case _: String =>
          (key, toStringList(list))
        case _: Boolean =>
          (key, toStringList(list))

        case _: Int =>
          (key, toNumList(list))
        case _: Double =>
          (key, toNumList(list))
        case _: Long =>
          (key, toNumList(list))
        case _ =>
          (key, CustomFieldEntryText(""))

      }.getOrElse((key, toNumList(list)))
    }

    def toStringList(s:Seq[_<:Any]): CustomFieldEntry[_<:Any] = {
      CustomFieldEntryTextArray(s.map{
        case s:String => s
        case b:Boolean => b.toString.toLowerCase
        case s:Any => s.toString
      })
    }

    def toNumList(s:Seq[_<:Any]): CustomFieldEntry[_<:Any] = {
      CustomFieldEntryNumberArray(s.map{
        case in: Int => in.toDouble
        case in: Double => in
        case in: Long => in.toDouble
      })
    }

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
      case list:List[_] =>
        handleSeq(customFields._1,list)
      case list:Seq[_] =>
        handleSeq(customFields._1,list)
      case in:Any =>
        ( customFields._1, CustomFieldEntryText(in.toString))
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