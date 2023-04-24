/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers.domain

import org.joda.time.DateTime

/**
  * The basic event
  * @param memberRefId Your member identifier reference
  * @param entityRefId The reference to the product in your system
  * @param eventRefId The reference to this event in your system
  * @param batchId [Optional] The identifier used to link related events together
  * @param action The type of action, login, buy, wager etc
  * @param sourceValue The value assigned to this event
  * @param transactionTimestamp The date and time in UTC when this transaction occurred
  * @param tags Additional meta data describing this event
  * @param metadata Additional meta data in key value format describing this event
  */
case class BasicEventModel(
													memberId: Option[String],
													memberRefId: String,
													entityRefId: String,
													eventRefId: String,
													batchId: Option[String],
													action: String,
													sourceValue: Double,
													transactionTimestamp: DateTime,
													tags: Seq[String] = Seq.empty,
													customFields: Map[String, CustomFieldEntry[_<:Any]] = Map.empty
													)

