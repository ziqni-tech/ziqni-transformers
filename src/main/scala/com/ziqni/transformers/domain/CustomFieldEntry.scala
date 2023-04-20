package com.ziqni.transformers.domain

abstract class CustomFieldEntry[T<:Any] (fieldType: String, value: T)
final case class CustomFieldEntryText(text: String) extends CustomFieldEntry[String](fieldType = "Text", value = text)
final case class CustomFieldEntryTextArray(texts: Seq[String]) extends CustomFieldEntry[Seq[String]](fieldType = "TextArray", value = texts)
final case class CustomFieldEntryNumber(number: Double) extends CustomFieldEntry[Double](fieldType = "Number", value = number)
final case class CustomFieldEntryNumberArray(numbers: Seq[Double]) extends CustomFieldEntry[Seq[Double]](fieldType = "NumberArray", value = numbers)

object CustomFieldEntry {

  private implicit def toText(v: String): CustomFieldEntry[_ <: Any] = CustomFieldEntryText(v)
  private implicit def toTextArr(v: Seq[String]): CustomFieldEntry[_ <: Any] = CustomFieldEntryTextArray(v)

  private implicit def toNumInt(v: Int): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumber(v)
  private implicit def toNumIntArr(v: Seq[Int]): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumberArray(v.map(i=>i.doubleValue()))

  private implicit def toNumDouble(v: Double): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumber(v)
  private implicit def toNumDoubleArr(v: Seq[Double]): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumberArray(v)

  private implicit def toNumLong(v: Long): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumber(v)
  private implicit def toNumLongArr(v: Seq[Long]): CustomFieldEntry[_ <: Any] = CustomFieldEntryNumberArray(v.map(i=>i.longValue()))
}
