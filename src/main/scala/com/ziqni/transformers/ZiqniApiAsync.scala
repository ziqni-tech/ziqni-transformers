/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers

import com.ziqni.transformers.domain._

import java.nio.charset.Charset
import scala.concurrent.Future

trait ZiqniApiAsync extends ZiqniApiHttp {

	/** *
	  * Generate a unique time based UUID, this can be used to set the batchId value if
	  * a single event is transformed into multiple distinct events (facts) and a correlation
	  * needs to be maintained
	  *
	  * @return A time based UUID as a string
	  */
	def nextId: String

	/**
	  * Your account identifier
	  */
	def accountId: String

	/**
	  * Get the space name associated with this account
	  */
	def spaceName: String

	/**
	  * Get sub accounts for this master account if any exists
	  */
	def subAccounts: Map[String, ZiqniApiAsync]

	def getSubAccount(spaceName: String): Option[ZiqniApiAsync]

	/**
	  * Insert an event into your Ziqni space
	  *
	  * @param event The event to add
	  * @return True on success, false on duplicate and exception if malformed
	  */
	def pushEventAsync(event: BasicEventModel): Future[Boolean]

	/**
	  * Insert a sequence of events into your Ziqni space
	  *
	  * @param events The events to add
	  * @return True on success, false on duplicate and exception if malformed
	  */
	def pushEventsAsync(events: Seq[BasicEventModel]): Future[Boolean]

	/**
	  * Insert an event into your Ziqni space
	  *
	  * @param event The event to add
	  * @param delay The time in milliseconds to delay processing of event
	  * @return True on success, false on duplicate and exception if malformed
	  */
	def pushEventWithDelayAsync(event: BasicEventModel, delay: Long): Future[Boolean]

	/**
	  * Insert a sequence of events into your Ziqni space
	  *
	  * @param events The events to add
	  * @param delay  The time in milliseconds to delay processing of event
	  * @return True on success, false on duplicate and exception if malformed
	  */
	def pushEventsWithDelayAsync(events: Seq[BasicEventModel], delay: Long): Future[Boolean]

	/**
	  * Get the Ziqni id for the member based on your reference id
	  *
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @return The id used in the Ziqni system or None if the user does not exist
	  */
	def memberIdFromMemberRefIdAsync(memberReferenceId: String): Future[Option[String]]

	/**
	  * Get the member reference id for the member based on Ziqni id
	  *
	  * @param memberId The id used to identify this member in the sending system
	  * @return The id used in the Ziqni system or None if the user does not exist
	  */
	def memberRefIdFromMemberIdAsync(memberId: String): Future[Option[String]]

	/**
	  * Create a member in the Ziqni system
	  *
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @param displayName       Display name
	  * @param groups            The groups to add this member to
	  * @return The id used in the Ziqni system
	  */
	def createMemberAsync(memberReferenceId: String, displayName: String, groups: Seq[String], metaData: Option[Map[String, String]] = None): Future[Option[String]]

	/**
	  *
	  * @param clMemberId     CL Member Id
	  * @param displayName    Display name
	  * @param groupsToUpdate The groups to add this member to
	  * @return The id used in the Ziqni system
	  */
	def updateMemberAsync(clMemberId: String, memberReferenceId: Option[String], displayName: Option[String], groupsToUpdate: Option[Array[String]], metaData: Option[Map[String, String]]): Future[Option[String]]

	/**
	  *
	  * @param memberId Ziqni Reward Id
	  * @return BasicMemberModel returns a basic member object
	  */
	def getMemberAsync(memberId: String): Future[Option[BasicMemberModel]]

	/**
	  * Get the Ziqni id for the product based on your reference id
	  *
	  * @param productReferenceId The id used to identify this product in the sending system
	  * @return The id used in the Ziqni system or None if the product does not exist
	  */
	def productIdFromProductRefIdAsync(productReferenceId: String): Future[Option[String]]

	/**
	  * Get the product id for the product based on your Ziqni id
	  *
	  * @param productId The id used to identify this product in the sending system
	  * @return The id used in the Ziqni system or None if the product does not exist
	  */
	def productRefIdFromProductIdAsync(productId: String): Future[Option[String]]

	/**
	  *
	  * @param productReferenceId      The id used to identify this product in the sending system
	  * @param displayName             Display name
	  * @param providers               The providers of this product
	  * @param productType             The type of product
	  * @param defaultAdjustmentFactor The default adjustment factor to apply
	  * @return The id used in the Ziqni system
	  */
	def createProductAsync(productReferenceId: String, displayName: String, providers: Seq[String], productType: String, defaultAdjustmentFactor: Double, metaData: Option[Map[String, String]] = None): Future[Option[String]]

	/**
	  *
	  * @param clProductId             CL Product Id
	  * @param displayName             Display name
	  * @param providers               The providers of this product
	  * @param productType             The type of product
	  * @param defaultAdjustmentFactor The default adjustment factor to apply
	  * @return The id used in the Ziqni system
	  */
	def updateProductAsync(clProductId: String, productReferenceId: Option[String], displayName: Option[String], providers: Option[Array[String]], productType: Option[String], defaultAdjustmentFactor: Option[Double], metaData: Option[Map[String, String]]): Future[Option[String]]

	/**
	  *
	  * @param productId Ziqni Product Id
	  * @return BasicProductModel returns a basic product object
	  */
	def getProductAsync(productId: String): Future[Option[BasicProductModel]]

	/**
	  * Verify if the event action type exists in your space
	  *
	  * @param action The action
	  * @return True of the action was created
	  */
	def eventActionExistsAsync(action: String): Future[Future[Boolean]]

	/** *
	  * Create the action in your space
	  *
	  * @param action True on success false on failure
	  * @return
	  */
	def createEventActionAsync(action: String, name: Option[String] = None, metaData: Option[Map[String, String]] = None): Future[Boolean]

	/** *
	  * Update the action in your space
	  *
	  * @param action True on success false on failure
	  * @return
	  */
	def updateEventActionAsync(action: String, name: Option[String] = None, metaData: Option[Map[String, String]] = None, unitOfMeasureType: Option[String] = None): Future[Boolean]

	/**
	  *
	  * @param achievementId Ziqni Achievement Id
	  * @return BasicAchievementModel returns a basic achievement object
	  */
	def getAchievementAsync(achievementId: String): Future[Option[BasicAchievementModel]]

	/**
	  *
	  * @param contestId Ziqni Contest Id
	  * @return BasicContestModel returns a basic contest object
	  */
	def getContestAsync(contestId: String): Future[Option[BasicContestModel]]

	/**
	  *
	  * @param rewardId Ziqni Reward Id
	  * @return BasicRewardModel returns a basic reward object
	  */
	def getRewardAsync(rewardId: String): Future[Option[BasicRewardModel]]

	/**
	  *
	  * @param awardId Ziqni Award Id
	  * @return BasicAwardModel returns a basic award object
	  */
	def getAwardAsync(awardId: String): Future[Option[BasicAwardModel]]

	/**
	  *
	  * @param unitOfMeasureId Ziqni Unit of Measure Id
	  * @return BasicUnitOfMeasureModel returns a basic unit of measure object
	  */
	def getUnitOfMeasureAsync(unitOfMeasureId: String): Future[Option[BasicUnitOfMeasureModel]]

	/**
	  *
	  * @param unitOfMeasureKey Ziqni UoM key
	  * @return Double returns a multiplier associated with the UoM
	  */
	def getUoMMultiplierFromKeyAsync(unitOfMeasureKey: String): Future[Option[Double]]

	/**
	  * Converts a json string to a JValue
	  *
	  * @param body The string to deserialise
	  * @return JValue or throws exception
	  */
	def fromJsonString(body: String): JValue

	/**
	  * Converts a map to a json string
	  *
	  * @param obj The object to serialise
	  * @return json string or throws exception
	  */
	def toJsonFromMap(obj: Map[String, Any]): String

	/**
	  * Converts byte array to String using UTF-8
	  *
	  * @param body    The string encoded as bytes
	  * @param charset Optional, character set to decode byte array, default is UTF-8
	  * @return Decoded string or throws exception
	  */
	def convertByteArrayToString(body: Array[Byte], charset: String = "UTF-8"): String = new String(body, Charset.forName(charset))

}