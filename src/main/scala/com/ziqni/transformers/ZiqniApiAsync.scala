/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers

import com.ziqni.transformers.domain._
import jdk.jfr.internal.LogLevel

import scala.concurrent.Future

trait ZiqniApiAsync {

	/**
		* Insert an event into your Ziqni space
		*
		* @param event The event to add
		* @return True on success, false on duplicate and exception if malformed
		*/
	def pushEvent(event: ZiqniEvent): Future[Boolean]

	/**
	  * Insert a sequence of events into your Ziqni space
	  *
	  * @param events The events to add
	  * @return True on success, false on duplicate and exception if malformed
	  */
	def pushEvents(events: Seq[ZiqniEvent]): Future[Boolean]

	/**
		* Insert a sequence of events into your Ziqni space
		* and also keep a local copy based on batch id for up to 5 minutes
		*
		* @param event The events to add
		* @return True on success, false on duplicate and exception if malformed
		*/
	def pushEventTransaction(event: ZiqniEvent): Future[Boolean]

	def findByBatchId(batchId: String): Future[Seq[ZiqniEvent]]

	/**
	  * Get the Ziqni id for the member based on your reference id
	  *
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @return The id used in the Ziqni system or None if the user does not exist
	  */
	def memberIdFromMemberRefId(memberReferenceId: String): Future[Option[String]]

	/**
	  * Get the member reference id for the member based on Ziqni id
	  *
	  * @param memberId The id used to identify this member in the sending system
	  * @return The id used in the Ziqni system or None if the user does not exist
	  */
	def memberRefIdFromMemberId(memberId: String): Future[Option[ZiqniMember]]

	/**
	  * Create a member in the Ziqni system
	  *
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @param displayName       Display name
	  * @param tags            The groups to add this member to
	  * @return The id used in the Ziqni system
	  */
	def createMember(toCreate: CreateMember): Future[Option[String]]

	/**
		* Get or create a member
		*
		* @param id            The id used to identify this member in the sending system
		* @param isReferenceId Is the id the ZIQNI id or a reference id
		* @param createAs      The object to use when creating the product
		* @return The id used in the Ziqni system
		*/
	def getOrCreateMember(id: String, isReferenceId: Boolean, createAs: () => CreateMember): Future[ZiqniMember]

	/**
	  *
	  * @param clMemberId     CL Member Id
	  * @param displayName    Display name
	  * @param groupsToUpdate The groups to add this member to
	  * @return The id used in the Ziqni system
	  */
	def updateMember(memberId: String, memberReferenceId: Option[String], displayName: Option[String], tagsToUpdate: Option[Seq[String]], metaData: Option[Map[String, String]]): Future[Option[String]]

	/**
	  *
	  * @param memberId Ziqni Reward Id
	  * @return BasicMemberModel returns a basic member object
	  */
	def getMember(memberId: String): Future[Option[ZiqniMember]]

	/**
	  * Get the Ziqni id for the product based on your reference id
	  *
	  * @param productReferenceId The id used to identify this product in the sending system
	  * @return The id used in the Ziqni system or None if the product does not exist
	  */
	def productFromProductRefId(productReferenceId: String): Future[Option[ZiqniProduct]]

	/**
	  * Get the product id for the product based on your Ziqni id
	  *
	  * @param productId The id used to identify this product in the sending system
	  * @return The id used in the Ziqni system or None if the product does not exist
	  */
	def productRefIdFromProductId(productId: String): Future[Option[String]]

	/**
	  *
	  * @param productReferenceId      The id used to identify this product in the sending system
	  * @param displayName             Display name
	  * @param providers               The providers of this product
	  * @param productType             The type of product
	  * @param defaultAdjustmentFactor The default adjustment factor to apply
	  * @return The id used in the Ziqni system
	  */
	def createProduct(toCreate: CreateProduct): Future[Option[String]]

	/**
	  * Get or create a product
	  * @param id The id used to identify this product in the sending system
	  * @param isReferenceId Is the id the ZIQNI id or a reference id
	  * @param createAs The object to use when creating the product
	  * @return The id used in the Ziqni system
	  */
	def getOrCreateProduct(id: String, isReferenceId: Boolean, createAs: () => CreateProduct): Future[ZiqniProduct]

	/**
	  *
	  * @param clProductId             CL Product Id
	  * @param displayName             Display name
	  * @param providers               The providers of this product
	  * @param productType             The type of product
	  * @param defaultAdjustmentFactor The default adjustment factor to apply
	  * @return The id used in the Ziqni system
	  */
	def updateProduct(productId: String, productReferenceId: Option[String], displayName: Option[String], providers: Option[Seq[String]], productType: Option[String], defaultAdjustmentFactor: Option[Double], metaData: Option[Map[String, String]]): Future[Option[String]]


	/**
		* Delete product by id
		*
		* @param productId - Ziqni product id
		* @return
		*/
	def deleteProduct(productId: String): Future[Boolean]

	/**
	  *
	  * @param productId Ziqni Product Id
	  * @return BasicProductModel returns a basic product object
	  */
	def getProduct(productId: String): Future[Option[ZiqniProduct]]


	/**
	  * Verify if the event action type exists in your space
	  *
	  * @param action The action
	  * @return True of the action was created
	  */
	def eventActionExists(action: String): Future[Boolean]

	/** *
	  * Create the action in your space
	  *
	  * @param action True on success false on failure
	  * @return
	  */
	def createEventAction(action: String, name: Option[String], metaData: Option[Map[String, String]], unitOfMeasureKey: Option[String]): Future[Boolean]

	/** *
	  * Get or create the action in your space
	  *
	  * @param action True on success false on failure
	  * @return
	  */
	def getOrCreateEventAction(action: String, createAs: () => CreateEventAction): Future[String]

	/** *
	  * Update the action in your space
	  *
	  * @param action True on success false on failure
	  * @return
	  */
	def updateEventAction(action: String, name: Option[String], metaData: Option[Map[String, String]], unitOfMeasureType: Option[String]): Future[Boolean]

	/**
	  *
	  * @param achievementId Ziqni Achievement Id
	  * @return BasicAchievementModel returns a basic achievement object
	  */
	def getAchievement(achievementId: String): Future[Option[ZiqniAchievement]]

	/**
	  *
	  * @param contestId Ziqni Contest Id
	  * @return BasicContestModel returns a basic contest object
	  */
	def getContest(contestId: String): Future[Option[ZiqniContest]]

	/**
	  *
	  * @param rewardId Ziqni Reward Id
	  * @return BasicRewardModel returns a basic reward object
	  */
	def getReward(rewardId: String): Future[Option[ZiqniReward]]

	/**
	  *
	  * @param awardId Ziqni Award Id
	  * @return BasicAwardModel returns a basic award object
	  */
	def getAward(awardId: String): Future[Option[ZiqniAward]]

	/**
	  *
	  * @param unitOfMeasureId Ziqni Unit of Measure Id
	  * @return BasicUnitOfMeasureModel returns a basic unit of measure object
	  */
	def getUnitOfMeasure(unitOfMeasureId: String): Future[Option[ZiqniUnitOfMeasure]]

	/**
		*
		* @param key The key used to identify this UoM
		* @param name The name to give this unit of measure
		* @param isoCode The ISO code
		* @param multiplier The points multiplier
		* @param unitOfMeasureType The type [OTHER, CURRENCY, MASS, TIME, TEMPERATURE, ELECTRICCURRENT, AMOUNTOFSUBSTANCE, LUMINOUSINTENSITY, DISTANCE]
		* @return
		*/
	def createUnitOfMeasure(key: String, name: String, isoCode: Option[String], multiplier: Double, unitOfMeasureType: Option[String]): Future[Option[String]]

	/**
		* Get or create unit of measure
		* @param key The key used to identify this UoM
		* @param name The name to give this unit of measure
		* @param isoCode The ISO code
		* @param multiplier The points multiplier
		* @param unitOfMeasureType The type [OTHER, CURRENCY, MASS, TIME, TEMPERATURE, ELECTRICCURRENT, AMOUNTOFSUBSTANCE, LUMINOUSINTENSITY, DISTANCE]
		* @return
		*/
	def getOrCreateUnitOfMeasure(key: String, createAs: () => CreateUnitOfMeasure): Future[ZiqniUnitOfMeasure]

	/**
	  *
	  * @param unitOfMeasureKey Ziqni UoM key
	  * @return Double returns a multiplier associated with the UoM
	  */
	def getUoMMultiplierFromKey(unitOfMeasureKey: String): Future[Option[Double]]

	def writeToSystemLog(throwable: Throwable, logLevel: LogLevel): Unit
}