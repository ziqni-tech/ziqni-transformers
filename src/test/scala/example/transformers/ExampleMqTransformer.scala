/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package example.transformers

// Do not add additional imports from non-standard libraries
import com.ziqni.transformers._
import com.ziqni.transformers.domain.BasicEventModel
import org.joda.time.DateTime

class ExampleMqTransformer extends ZiqniMqTransformer {

	override def apply(message: Array[Byte], ziqniApi: ZiqniApi, args: Map[String, Any]): Unit = {

		////////////////////////////////////////////////////
		// 1. Extract the main data from our json body
		////////////////////////////////////////////////////


		val messageAsString = ziqniApi.convertByteArrayToString(message)
		val messageAsJson = ziqniApi.fromJsonString(messageAsString)
		val jsonObj = messageAsJson.\("transaction")

		val action = Json.getFromJValue[String](jsonObj, "action")
		val batchId = Json.getFromJValue[String](jsonObj, "round_id")
		val gameType = Json.getFromJValue[String](jsonObj, "game_type")
		val sourceValue = Json.getFromJValue[Double](jsonObj, "bet_cents")
		val memberRefId = Json.getFromJValue[String](jsonObj, "player_id")
		val entityRefId = Json.getFromJValue[Int](jsonObj, "game_id").toString
		val eventRefId = Json.getFromJValue[String](jsonObj, "transaction_id")
		val transactionTimestamp = new DateTime(Json.getFromJValue[String](jsonObj, "timestamp"))

		////////////////////////////////////////////////////
		// 2. Create data points if they are missing
		////////////////////////////////////////////////////

		ziqniApi.memberIdFromMemberRefId(memberRefId).getOrElse{
			// Create a new member  ->
			ziqniApi.createMember(memberRefId, "unknown", Seq("new"))
		}

		ziqniApi.productIdFromProductRefId(entityRefId).getOrElse{
			// Create a new product
			ziqniApi.createProduct(entityRefId, "unknown", Seq.empty, "slot", 0)
		}

		if(!ziqniApi.eventActionExists(action)) {
			// Create the action
			ziqniApi.createEventAction(action)
		}

		////////////////////////////////////////////////////
		// 3. Lets decorate the event with our custom fields
		////////////////////////////////////////////////////

		val customFields = new scala.collection.mutable.HashMap[String, Seq[Any]]()

		val skinId = Json.getFromJValue[Int](jsonObj, "skin_id")
		val freeSpins = Json.getFromJValue[Boolean](jsonObj, "freespins_flag")

		customFields.put("skin-id",Seq(skinId))
		customFields.put("game-type", Seq(gameType))
		customFields.put("free-spins", Seq(freeSpins))

		////////////////////////////////////////////////////
		// 4. Push the data to your CompetitionLabs space
		////////////////////////////////////////////////////

		val event = BasicEventModel(
			action = action,
			tags = Seq.empty,
			eventRefId = eventRefId,
			memberRefId = memberRefId,
			entityRefId = entityRefId,
			batchId = Option(batchId),
			sourceValue = sourceValue,
			metadata = customFields.toMap,
			transactionTimestamp = transactionTimestamp
		)

		ziqniApi.pushEvent(event)
	}
}
