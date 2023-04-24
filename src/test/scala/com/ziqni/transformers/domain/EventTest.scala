package com.ziqni.transformers.domain

import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization
import org.scalatest.funsuite.AnyFunSuiteLike

class EventTest extends AnyFunSuiteLike with CustomFieldEntryImplicits {

  test("testAsBasicEventModel") {
    val now = DateTime.now();

    val event = Event(
      memberRefId = "",
      action = "steps",
      batchId = Option("batchId"),
      entityRefId = "entityRefId",
      sourceValue = 123.45,
      transactionTimestamp = now,
      tags = Seq("tag1","tag2","tag3"),
      eventRefId = "",
      memberId = Option("memberId"),
      customFields = Map[String, Any](
        "number-1" -> 1,
        "number-2" -> 2.2,
        "number-3" -> Seq(1,2),
        "number-4" -> Seq(1.1,2),


        "text-1" -> "1",
        "text-2" -> "2.2",
        "text-3" -> Seq("1", "2"),
        "text-4" -> Seq(1.1, "2"),
      )
    )

    val json = Serialization.write(event)(DefaultFormats)
    val jsonString = json.toString
    val asBasicEventObject = event.asBasicEventModel

    assert(asBasicEventObject.memberId.get == "memberId")

  }

}
