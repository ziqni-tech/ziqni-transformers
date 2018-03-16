/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */
package com.competitionlabs.transformers

import java.nio.charset.Charset

import com.competitionlabs.transformers.domain.{BasicAchievementModel, BasicAwardModel, BasicEventModel, BasicRewardModel}
import org.joda.time.DateTime
import org.json4s.JsonAST.JValue

trait CompetitionLabsApi {

	/***
	  * Generate a unique time based UUID, this can be used to set the batchId value if
	  * a single event is transformed into multiple distinct events (facts) and a correlation
	  * needs to be maintained
	  * @return A time based UUID as a string
	  */
	def nextId: String

	/**
	  * Your account identifier
	  */
	val accountId: String

	/**
	  * Get the space name associated with this account
	  */
	val spaceName: String

	/**
	  * Insert an event into your CompetitionLabs space
	  * @param event The event to add
	  * @return True on success, false on duplicate and exception if malformed
	  */
	def pushEvent(event: BasicEventModel): Boolean

	/**
	  * Insert a sequence of events into your CompetitionLabs space
	  * @param events The events to add
	  * @return True on success, false on duplicate and exception if malformed
	  */
	def pushEvents(events: Seq[BasicEventModel]): Boolean

	/**
	  * Get the CompetitionLabs id for the member based on your reference id
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @return The id used in the CompetitionLabs system or None if the user does not exist
	  */
	def memberIdFromMemberRefId(memberReferenceId: String): Option[String]

	/**
	  * Get the member reference id for the member based on CompetitionLabs id
	  * @param memberId The id used to identify this member in the sending system
	  * @return The id used in the CompetitionLabs system or None if the user does not exist
	  */
	def memberRefIdFromMemberId(memberId: String): Option[String]

	/**
	  * Create a member in the CompetitionLabs system
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @param displayName Display name
	  * @param groups The groups to add this member to
	  * @return The id used in the CompetitionLabs system
	  */
	def createMember(memberReferenceId: String, displayName: String, groups:Seq[String]): Option[String]

	/**
	  * Get the CompetitionLabs id for the product based on your reference id
	  * @param productReferenceId The id used to identify this product in the sending system
	  * @return The id used in the CompetitionLabs system or None if the product does not exist
	  */
	def productIdFromProductRefId(productReferenceId: String): Option[String]

	/**
	  * Get the product id for the product based on your CompetitionLabs id
	  * @param productId The id used to identify this product in the sending system
	  * @return The id used in the CompetitionLabs system or None if the product does not exist
	  */
	def productRefIdFromProductId(productId: String): Option[String]

	/**
	  *
	  * @param productReferenceId The id used to identify this product in the sending system
	  * @param displayName Display name
	  * @param providers The providers of this product
	  * @param productType The type of product
	  * @param defaultAdjustmentFactor The default adjustment factor to apply
	  * @return The id used in the CompetitionLabs system
	  */
	def createProduct(productReferenceId: String, displayName: String, providers:Seq[String], productType: String, defaultAdjustmentFactor: Double): Option[String]

	/**
	  * Verify if the event action type exists in your space
	  * @param action The action
	  * @return True of the action was created
	  */
	def eventActionExists(action: String): Boolean

	/***
	  * Create the action in your space
	  * @param action True on success false on failure
	  * @return
	  */
	def createEventAction(action: String): Boolean
	
	/**
	  * [PROTOTYPE]
	  * Get the spot rate for a currency at a point in time from Oanda
	  * @param fromCurrency From currency ISO3 code
	  * @param toCurrency To currency ISO3 code
	  * @return The rate
	  */
	def spotExchangeRate(fromCurrency: String, toCurrency: String, pointInTime: DateTime): Double

	/**
	  *
	  * @param achievementId CompetitionLabs Achievement Id
	  * @return BasicAchievementModel returns a basic achievement object
	  */
	def getAchievement(achievementId: String): Option[BasicAchievementModel]

	/**
	  *
	  * @param rewardId CompetitionLabs Reward Id
	  * @return BasicAchievementModel returns a basic achievement object
	  */
	def getReward(rewardId: String): Option[BasicRewardModel]

	/**
	  *
	  * @param awardId CompetitionLabs Reward Id
	  * @return BasicAchievementModel returns a basic achievement object
	  */
	def getAward(awardId: String): Option[BasicAwardModel]

	/**
	  * Converts a json string to a JValue
	  * @param body The string to deserialise
	  * @return JValue or throws exception
	  */
	def fromJsonString(body: String): JValue

	/**
	  * Converts a map to a json string
	  * @param obj The object to serialise
	  * @return json string or throws exception
	  */
	def toJsonFromMap(obj: Map[String, Any]): String

	/**
	  * Converts byte array to String using UTF-8
	  * @param body The string encoded as bytes
	  * @param charset Optional, character set to decode byte array, default is UTF-8
	  * @return Decoded string or throws exception
	  */
	def convertByteArrayToString(body: Array[Byte], charset: String = "UTF-8"): String = new String(body, Charset.forName(charset))


}
