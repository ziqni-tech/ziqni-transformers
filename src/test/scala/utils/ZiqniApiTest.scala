/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package utils

import com.ziqni.transformers.domain._
import com.ziqni.transformers.ZiqniApi
import org.joda.time.DateTime
import org.json4s.{DefaultFormats, JsonAST}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jackson.Serialization

class ZiqniApiTest extends ZiqniApi {

	implicit val formats = DefaultFormats

	type HttpBody = String
	type UrlString = String
	type HeadersMap = Map[String, Seq[String]]

	val memberForTest = new mutable.HashMap[String, String]()

	val productForTest = new mutable.HashMap[String, String]()

	val eventActionsForTest = new ListBuffer[String]

	val eventsReceivedForTest = new mutable.HashMap[String, BasicEventModel]()

	val httpRequests = new mutable.HashMap[UrlString, (HttpBody, HeadersMap)]()

	/**
	  * Your account identifier
	  */
	override val accountId: String = "yourAccountId"

	/**
	  * Insert an event into your Ziqni space
	  *
	  * @param event The event to add
	  * @return True on success, false on duplicate and exception if malformed
	  */
	override def pushEvent(event: BasicEventModel): Boolean = {
		assert(eventActionExists(event.action))
		assert(productIdFromProductRefId(event.entityRefId).nonEmpty)
		assert(memberIdFromMemberRefId(event.memberRefId).nonEmpty)
		assert(event.sourceValue >= 0)

		eventsReceivedForTest.put(event.entityRefId, event).nonEmpty
	}

	/**
	  * Insert a sequence of events into your Ziqni space
	  *
	  * @param events The events to add
	  * @return True on success, false on duplicate and exception if malformed
	  */
	override def pushEvents(events: Seq[BasicEventModel]): Boolean = !events.forall(pushEvent)

	/**
	  * Get the Ziqni id for the member based on your reference id
	  *
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @return The id used in the Ziqni system or None if the user does not exist
	  */
	override def memberIdFromMemberRefId(memberReferenceId: String): Option[String] = memberForTest.get(memberReferenceId)

	/**
	  * Create a member in the Ziqni system
	  *
	  * @param memberReferenceId The id used to identify this member in the sending system
	  * @param displayName       Display name
	  * @param groups            The groups to add this member to
	  * @return The id used in the Ziqni system
	  */
	override def createMember(memberReferenceId: String, displayName: String, tags: Seq[String], metaData: Option[Map[String, String]] = None): Option[String] = {
		val key = "Z-" + memberReferenceId
		memberForTest.put(
			memberReferenceId, key
		)
		Option(key)
	}

	/**
	  *
	  * @param displayName Display name
	  * @param groups      The groups to add this member to
	  * @return The id used in the Ziqni system
	  */
	override def updateMember(memberId: String, memberReferenceId: Option[String], displayName: Option[String], tags: Option[Seq[String]], metaData: Option[Map[String, String]]): Option[String] = {
		val key = "Z-" + displayName
		memberForTest.put(
			memberReferenceId.get, displayName.get
		)
		Option(key)
	}

	/**
	  *
	  * @param memberId Ziqni Reward Id
	  * @return BasicMemberModel returns a basic member object
	  */
	override def getMember(memberId: String): Option[BasicMemberModel] = None


	/**
	  * Get the Ziqni id for the product based on your reference id
	  *
	  * @param productReferenceId The id used to identify this product in the sending system
	  * @return The id used in the Ziqni system or None if the product does not exist
	  */
	override def productIdFromProductRefId(productReferenceId: String): Option[String] = productForTest.get(productReferenceId)

	/**
	  *
	  * @param productReferenceId      The id used to identify this product in the sending system
	  * @param displayName             Display name
	  * @param providers               The providers of this product
	  * @param productType             The type of product
	  * @param defaultAdjustmentFactor The default adjustment factor to apply
	  * @return The id used in the Ziqni system
	  */
	override def createProduct(productReferenceId: String, displayName: String, providers: Seq[String], productType: String, defaultAdjustmentFactor: Double, metaData: Option[Map[String, String]] = None): Option[String] = {
		val key = "Z-" + productReferenceId
		productForTest.put(productReferenceId, key)
		Option(key)
	}

	/**
	  *
	  * @param productId Ziqni Product Id
	  * @return BasicProductModel returns a basic product object
	  */
	override def getProduct(productId: String): Option[BasicProductModel] = None

	/**
	  *
	  * @param displayName             Display name
	  * @param providers               The providers of this product
	  * @param productType             The type of product
	  * @param defaultAdjustmentFactor The default adjustment factor to apply
	  * @return The id used in the Ziqni system
	  */
	override def updateProduct(productId: String, productReferenceId: Option[String], displayName: Option[String], providers: Option[Seq[String]], productType: Option[String], defaultAdjustmentFactor: Option[Double], metaData: Option[Map[String, String]]): Option[String] = {
		val key = "Z-" + productReferenceId
		productForTest.put(productReferenceId.get, displayName.get)
		Option(key)
	}


	/**
	 * Delete product by id
	 *
	 * @param productId - Ziqni product id
	 * @return
	 */
	override def deleteProduct(productId: String): Boolean = true

	/**
	  * Verify if the event action type exists in your space
	  *
	  * @param action The action
	  * @return True of the action was created
	  */
	override def eventActionExists(action: String): Boolean = eventActionsForTest.contains(action)

	/** *
	  * Create the action in your space
	  *
	  * @param action True on success false on failure
	  * @return
	  */
	override def createEventAction(action: String, name: Option[String] = None, metaData: Option[Map[String, String]] = None): Boolean = {
		eventActionsForTest += action
		true
	}

	/** *
	  * Generate a unique time based UUID, this can be used to set the batchId value if
	  * a single event is transformed into multiple distinct events (facts) and a correlation
	  * needs to be maintained
	  *
	  * @return A time based UUID as a string
	  */
	override def nextId = "ABCDEFGHIK"

	/**
	  * Get the space name associated with this account
	  */
	override val spaceName: String = "yourspace"

	/**
	  * Get the member reference id for the member based on Ziqni id
	  *
	  * @param memberId The id used to identify this member in the sending system
	  * @return The id used in the Ziqni system or None if the user does not exist
	  */
	override def memberRefIdFromMemberId(memberId: String): Option[String] = memberForTest.values.find(_ == memberId)

	/**
	  * Get the product id for the product based on your Ziqni id
	  *
	  * @param productId The id used to identify this product in the sending system
	  * @return The id used in the Ziqni system or None if the product does not exist
	  */
	override def productRefIdFromProductId(productId: String): Option[String] = productForTest.values.find(_ == productId)

	/**
	  * Converts a json string to a JValue
	  *
	  * @param body The string to deserialise
	  * @return JValue or throws exception
	  */
	override def fromJsonString(body: String): JsonAST.JValue = parse(body)

	/**
	  * Converts a map to a json string
	  *
	  * @param obj The object to serialise
	  * @return json string or throws exception
	  */
	override def toJsonFromMap(obj: Map[String, Any]): String = Serialization.write(obj)

	/**
	  * Send a http get request
	  *
	  * @param url                  The url to send the request too
	  * @param headers              The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return The http status code or -1 on error
	  */
	override def httpGet(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[BasicAuthCredentials] = None, sendCompressed: Boolean = true): HttpResponseEntity = {
		httpRequests.put(url, ("", headers))
		HttpResponseEntity("This is test", 200)
	}

	/**
	  * Send a http put request
	  *
	  * @param url                  The url to send the request too
	  * @param body                 The request body
	  * @param headers              The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return The http status code or -1 on error
	  */
	override def httpPut(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[BasicAuthCredentials] = None, sendCompressed: Boolean = true): HttpResponseEntity = {
		httpRequests.put(url, (body, headers))
		HttpResponseEntity("This is test", 200)
	}

	/**
	  * Send a http post request
	  *
	  * @param url                  The url to send the request too
	  * @param body                 The request body
	  * @param headers              The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return The http status code or -1 on error
	  */
	override def httpPost(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[BasicAuthCredentials] = None, sendCompressed: Boolean = true): HttpResponseEntity = {
		httpRequests.put(url, (body, headers))
		HttpResponseEntity("This is test", 200)
	}

	/**
	  * Send a http delete request
	  *
	  * @param url                  The url to send the request too
	  * @param headers              The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return The http status code or -1 on error
	  */
	override def httpDelete(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[BasicAuthCredentials] = None, sendCompressed: Boolean = true): HttpResponseEntity = {
		httpRequests.put(url, ("", headers))
		HttpResponseEntity("This is test", 200)
	}

	/**
	  *
	  * @param achievementId Ziqni Achievement Id
	  * @return BasicAchievementModel returns a basic achievement object
	  */
	override def getAchievement(achievementId: String): Option[BasicAchievementModel] = None

	/**
	  *
	  * @param rewardId Ziqni Reward Id
	  * @return BasicAchievementModel returns a basic achievement object
	  */
	override def getReward(rewardId: String): Option[BasicRewardModel]= None

	/**
	  *
	  * @param awardId Ziqni Reward Id
	  * @return BasicAchievementModel returns a basic achievement object
	  */
	override def getAward(awardId: String): Option[BasicAwardModel] = None

	/**
	  * Get sub accounts for this master account if any exists
	  */
	override def subAccounts: Map[String, ZiqniApi] = Map.empty

	override def getSubAccount(spaceName: String): Option[ZiqniApi] = None

	/** *
	  * Update the action in your space
	  *
	  * @param action True on success false on failure
	  * @return
	  */
	override def updateEventAction(action: String, name: Option[String], metaData: Option[Map[String, String]], unitOfMeasureType: Option[String]): Boolean = false

	/**
	  *
	  * @param contestId Ziqni Contest Id
	  * @return BasicContestModel returns a basic contest object
	  */
	override def getContest(contestId: String): Option[BasicContestModel] = None

	/**
	  *
	  * @param unitOfMeasureId Ziqni Unit of Measure Id
	  * @return BasicUnitOfMeasureModel returns a basic unit of measure object
	  */
	override def getUnitOfMeasure(unitOfMeasureId: String): Option[BasicUnitOfMeasureModel] = None

	/**
	  *
	  * @param unitOfMeasureKey Ziqni UoM key
	  * @return Double returns a multiplier associated with the UoM
	  */
	override def getUoMMultiplierFromKey(unitOfMeasureKey: String): Option[Double] = None
}
