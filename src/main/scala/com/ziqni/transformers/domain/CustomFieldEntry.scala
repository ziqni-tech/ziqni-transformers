package com.ziqni.transformers.domain
/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2023
  */
import scala.language.implicitConversions

abstract class CustomFieldEntry[T<:Any] (fieldType: String, val value: T)
final case class CustomFieldEntryText(text: String) extends CustomFieldEntry[String](fieldType = "Text", value = text)
final case class CustomFieldEntryTextArray(texts: Seq[String]) extends CustomFieldEntry[Seq[String]](fieldType = "TextArray", value = texts)
final case class CustomFieldEntryNumber(number: Double) extends CustomFieldEntry[Double](fieldType = "Number", value = number)
final case class CustomFieldEntryNumberArray(numbers: Seq[Double]) extends CustomFieldEntry[Seq[Double]](fieldType = "NumberArray", value = numbers)

trait CustomFieldEntryImplicits {

  type CustomFieldEntryMap = Map[String, CustomFieldEntry[_<:Any]]

  implicit def toTextBoolean(v: Option[Boolean]): CustomFieldEntry[_ <: Any] = CustomFieldEntryText(v.map(_.toString.toLowerCase()).orNull)
  implicit def toTextBoolean(v: Boolean): CustomFieldEntry[_ <: Any] = CustomFieldEntryText(v.toString.toLowerCase())

  implicit def toTextOpt(v: Option[String]): CustomFieldEntry[_ <: Any] = CustomFieldEntryText(v.orNull)
  implicit def toText(v: String): CustomFieldEntry[_ <: Any] = CustomFieldEntryText(v)
  implicit def toTextArr(v: Seq[String]): CustomFieldEntry[_ <: Any] = CustomFieldEntryTextArray(v)

  implicit def toNumInt(v: Int): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumber(v)
  implicit def toNumIntArr(v: Seq[Int]): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumberArray(v.map(i=>i.doubleValue()))
  implicit def toNumIntArrList(v: List[Int]): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumberArray(v.map(i=>i.doubleValue()))

  implicit def toNumDouble(v: Double): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumber(v)
  implicit def toNumDoubleFromOption(v: Option[Double]): CustomFieldEntryNumber = CustomFieldEntryNumber(v.getOrElse(0.0))
  implicit def toNumDoubleArr(v: Seq[Double]): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumberArray(v)

  implicit def toNumLong(v: Long): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumber(v)
  implicit def toNumLongArr(v: Seq[Long]): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumberArray(v.map(i=>i.longValue()))
}
