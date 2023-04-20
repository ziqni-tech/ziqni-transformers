package com.ziqni.transformers.domain

abstract class CustomFieldEntry[T<:Any] (fieldType: String, value: T)
final case class CustomFieldEntryText(text: String) extends CustomFieldEntry[String](fieldType = "Text", value = text)
final case class CustomFieldEntryTextArray(texts: Seq[String]) extends CustomFieldEntry[Seq[String]](fieldType = "TextArray", value = texts)
final case class CustomFieldEntryNumber(number: Double) extends CustomFieldEntry[Double](fieldType = "Number", value = number)
final case class CustomFieldEntryNumberArray(numbers: Seq[Double]) extends CustomFieldEntry[Seq[Double]](fieldType = "NumberArray", value = numbers)
