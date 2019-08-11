/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2019
  */
package example.transformers

// Do not add additional imports from non-standard libraries
import com.competitionlabs.transformers._
import com.competitionlabs.transformers.domain.BasicEventModel
import org.joda.time.DateTime

class exampleMQTransformer extends CLMQTransformer {

	override def apply(message: Array[Byte], competitionLabsApi: CompetitionLabsApi, args: Map[String, Any]): Unit = {

		////////////////////////////////////////////////////
		// 1. Extract the main data from our json body
		////////////////////////////////////////////////////


		val messageAsString = competitionLabsApi.convertByteArrayToString(message)
		val messageAsJson = competitionLabsApi.fromJsonString(messageAsString)
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

		competitionLabsApi.memberIdFromMemberRefId(memberRefId).getOrElse{
			// Create a new member  ->
			competitionLabsApi.createMember(memberRefId, "unknown", Seq("new"))
		}

		competitionLabsApi.productIdFromProductRefId(entityRefId).getOrElse{
			// Create a new product
			competitionLabsApi.createProduct(entityRefId, "unknown", Seq.empty, "slot", 0)
		}

		if(!competitionLabsApi.eventActionExists(action)) {
			// Create the action
			competitionLabsApi.createEventAction(action)
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

		competitionLabsApi.pushEvent(event)
	}
}
