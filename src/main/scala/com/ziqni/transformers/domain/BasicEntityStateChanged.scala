package com.ziqni.transformers.domain

import org.joda.time.DateTime

case class BasicEntityStateChanged(accountId: String, changedAt: DateTime, changedBy: String, displayName: String, entityId: String, entityParentId: String,
                                   entityRefId: String, entityType: String, metadata: Map[String,String], sequenceNumber: Long, typeOffChange: Int, previousState: Int, currentState: Int, memberId: String)
