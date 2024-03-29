/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers

import com.ziqni.transformers.domain._
import org.joda.time.DateTime

trait ZiqniApi {

	/** *
	  * Generate a unique time based UUID, this can be used to set the batchId value if
	  * a single event is transformed into multiple distinct events (facts) and a correlation
	  * needs to be maintained
	  *
	  * @return A time based UUID as a string
	  */
	def nextId: String

	/**
	  * Get sub accounts for this master account if any exists
	  */
	def subAccounts: Map[String, ZiqniApi]

	/**
	 * Get sub account api by space name
	 * @param spaceName
	 * @return
	 */
	def getSubAccount(spaceName: String): Option[ZiqniApi]

	/**
		* DEPRECATED, use the async methods!
	  * Insert an event into your Ziqni space
	  *
	  * @param event The event to add
	  * @return True on success, false on duplicate and exception if malformed
	  */
	@Deprecated(since = "v1.0.6")
	def pushEvent(event: ZiqniEvent): Boolean

	/**
		* DEPRECATED, use the async methods!
	  * Insert a sequence of events into your Ziqni space
	  *
	  * @param events The events to add
	  * @return True on success, false on duplicate and exception if malformed
	  */
	@Deprecated(since = "v1.0.6")
	def pushEvents(events: Seq[ZiqniEvent]): Boolean

	/**
		* DEPRECATED, use the async methods!
	  * Get the Ziqni id for the member based on your reference id
	  *
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @return The id used in the Ziqni system or None if the user does not exist
	  */
	@Deprecated(since = "v1.0.6")
	def memberIdFromMemberRefId(memberReferenceId: String): Option[ZiqniMember]

	/**
		* DEPRECATED, use the async methods!
	  * Get the member reference id for the member based on Ziqni id
	  *
	  * @param memberId The id used to identify this member in the sending system
	  * @return The id used in the Ziqni system or None if the user does not exist
	  */
	@Deprecated(since = "v1.0.6")
	def memberRefIdFromMemberId(memberId: String): Option[String]

	/**
		* DEPRECATED, use the async methods!
	  * Create a member in the Ziqni system
	  *
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @param displayName       Display name
	  * @param tags            Tags to assign to the member
	  * @return The id used in the Ziqni system
	  */
	@Deprecated(since = "v1.0.6")
	def createMember(toCreate: CreateMemberRequest): Option[String]

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param memberId     	Ziqni Member Id
	  * @param displayName    	Display name
	  * @param tagsToUpdate 	Tags to assign to the member
	  * @return The id used in the Ziqni system
	  */
	@Deprecated(since = "v1.0.6")
	def updateMember(memberId: String, memberReferenceId: Option[String], displayName: Option[String], tagsToUpdate: Option[Seq[String]], metadata: Option[Map[String, String]]): Option[String]

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param memberId Ziqni member Id
	  * @return BasicMemberModel returns a basic member object
	  */
	@Deprecated(since = "v1.0.6")
	def getMember(memberId: String): Option[ZiqniMember]

	/**
		* DEPRECATED, use the async methods!
	  * Get the Ziqni id for the product based on your reference id
	  *
	  * @param productReferenceId The id used to identify this product in the sending system
	  * @return The id used in the Ziqni system or None if the product does not exist
	  */
	@Deprecated(since = "v1.0.6")
	def productFromProductRefId(productReferenceId: String): Option[ZiqniProduct]

	/**
		* DEPRECATED, use the async methods!
	  * Get the product id for the product based on your Ziqni id
	  *
	  * @param productId The id used to identify this product in the sending system
	  * @return The id used in the Ziqni system or None if the product does not exist
	  */
	@Deprecated(since = "v1.0.6")
	def productRefIdFromProductId(productId: String): Option[String]

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param productReferenceId      	The id used to identify this product in the sending system
	  * @param displayName             	Display name
	  * @param providers               The providers of this product
	  * @param productType             The type of product
	  * @param defaultAdjustmentFactor The default adjustment factor to apply
	  * @return The id used in the Ziqni system
	  */
	@Deprecated(since = "v1.0.6")
	def createProduct(toCreate: CreateProductRequest): Option[String]

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param productId             Ziqni Product Id
	  * @param displayName             Display name
	  * @param providers               The providers of this product
	  * @param productType             The type of product
	  * @param defaultAdjustmentFactor The default adjustment factor to apply
	  * @return The id used in the Ziqni system
	  */
	@Deprecated(since = "v1.0.6")
	def updateProduct(productId: String, productReferenceId: Option[String], displayName: Option[String], tags: Option[Seq[String]], defaultAdjustmentFactor: Option[Double], metadata: Option[Map[String, String]]): Option[String]

	/**
		* DEPRECATED, use the async methods!
		* Delete product by id
		*
		* @param productId - Ziqni product id
	 * @return
	 */
	@Deprecated(since = "v1.0.6")
	def deleteProduct(productId: String): Boolean

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param productId Ziqni Product Id
	  * @return BasicProductModel returns a basic product object
	  */
	@Deprecated(since = "v1.0.6")
	def getProduct(productId: String): Option[ZiqniProduct]

	/**
		* DEPRECATED, use the async methods!
	  * Verify if the event action type exists in your space
	  *
	  * @param action The action
	  * @return True of the action was created
	  */
	@Deprecated(since = "v1.0.6")
	def eventActionExists(action: String): Boolean

	/** *
		* DEPRECATED, use the async methods!
	  * Create the action in your space
	  *
	  * @param action True on success false on failure
	  * @return
	  */
	@Deprecated(since = "v1.0.6")
	def createEventAction(action: String, name: Option[String], metadata: Option[Map[String, String]], unitOfMeasureKey: Option[String]): Boolean

	/** *
		* DEPRECATED, use the async methods!
	  * Update the action in your space
	  *
	  * @param action True on success false on failure
	  * @return
	  */
	@Deprecated(since = "v1.0.6")
	def updateEventAction(action: String, name: Option[String], metadata: Option[Map[String, String]], unitOfMeasureType: Option[String]): Boolean

	/**
		* DEPRECATED, use the async methods!
	  * [PROTOTYPE]
	  * Get the spot rate for a currency at a point in time from Oanda
	  *
	  * @param fromCurrency From currency ISO3 code
	  * @param toCurrency   To currency ISO3 code
	  * @return The rate
	  */
	@Deprecated(since = "v1.0.6")
	def spotExchangeRate(fromCurrency: String, toCurrency: String, pointInTime: DateTime): Double

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param achievementId Ziqni Achievement Id
	  * @return BasicAchievementModel returns a basic achievement object
	  */
	@Deprecated(since = "v1.0.6")
	def getAchievement(achievementId: String): Option[ZiqniAchievement]

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param contestId Ziqni Contest Id
	  * @return BasicContestModel returns a basic contest object
	  */
	@Deprecated(since = "v1.0.6")
	def getContest(contestId: String): Option[ZiqniContest]

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param rewardId Ziqni Reward Id
	  * @return BasicRewardModel returns a basic reward object
	  */
	@Deprecated(since = "v1.0.6")
	def getReward(rewardId: String): Option[ZiqniReward]

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param awardId Ziqni Award Id
	  * @return BasicAwardModel returns a basic award object
	  */
	@Deprecated(since = "v1.0.6")
	def getAward(awardId: String): Option[ZiqniAward]

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param unitOfMeasureId Ziqni Unit of Measure Id
	  * @return BasicUnitOfMeasureModel returns a basic unit of measure object
	  */
	@Deprecated(since = "v1.0.6")
	def getUnitOfMeasure(unitOfMeasureId: String): Option[ZiqniUnitOfMeasure]

	/**
		* DEPRECATED, use the async methods!
	  *
	  * @param unitOfMeasureKey Ziqni UoM key
	  * @return Double returns a multiplier associated with the UoM
	  */
	@Deprecated(since = "v1.0.6")
	def getUoMMultiplierFromKey(unitOfMeasureKey: String): Option[Double]
}