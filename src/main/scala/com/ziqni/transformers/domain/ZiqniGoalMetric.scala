/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers.domain

import org.joda.time.DateTime

abstract class ZiqniGoalMetric {

  /**
   * The member identifier
   * @return memberId
   */
  def getMemberId: String

  /**
   * The entity identifier
   * @return entityId
   */
  def getEntityId: String

  /**
   * The current value assigned to this goal
   * @return value
   */
  def getValue: BigDecimal

  /**
   * What percentage of this goal has been completed
   * @return percentageComplete
   */
  def getPercentageComplete: Double

  /**
   * The most significant scores
   * @return mostSignificantScores
   */
  def getMostSignificantScores: List[Double]

  /**
   * Internal timestamp associated with this goal
   * @return timestamp
   */
  def getTimestamp: Long

  /**
   * A count of the total number of mutations this goal has undergone
   * @return updateCount
   */
  def getUpdateCount: Long

  /**
   * The type of entity
   * @return entityType
   */
  def getEntityType: String

  /**
   * A point in time marker
   * @return markerTimeStamp
   */
  def getMarkerTimeStamp: Long

  /**
   * Has the minimum requirements been met
   * @return goalReached
   */
  def getGoalReached: Boolean

  /**
   * The system status assigned to this goal
   * @return statusCode
   */
  def getStatusCode: Integer

  /**
   * The position of this record in a sorted index if relevant
   * @return position
   */
  def getPosition: Integer

  /**
   * The user defined values for this goal
   * @return userDefinedValues
   */
  def getUserDefinedValues: Map[String, Double]
}

