package com.ziqni.transformers.domain

sealed trait AwardStateActions {
  def code: Int
}

object AwardStateActions {
  case object Issued extends AwardStateActions { val code = 15 }
  case object Held extends AwardStateActions { val code = 25 }
  case object Claimed extends AwardStateActions { val code = 35 }
  case object Processing extends AwardStateActions { val code = 45 }
  case object Delivered extends AwardStateActions { val code = 55 }
  case object Completed extends AwardStateActions { val code = 65 }
  case object Expired extends AwardStateActions { val code = 100 }
  case object Cancelled extends AwardStateActions { val code = 101 }
  case object Declined extends AwardStateActions { val code = 102 }

  val values: Seq[AwardStateActions] = Seq(Issued, Held, Claimed, Processing, Delivered, Completed, Expired, Cancelled, Declined)

  def fromCode(code: Int): Option[AwardStateActions] = values.find(_.code == code)
}
