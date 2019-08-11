/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2019
  */
package example.test

import example.transformers.exampleMQTransformer
import org.scalatest._
import utils.CompetitionLabsApiTest

class exampleRabbitMQTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfterEach with BeforeAndAfterAll {

	describe("Test the message queue receiver implementation") {

		//  Run the test
		it("should receive a published a message and transform it into a CompetitionLabs event") {

			// Prepare the test
			val transformer = new exampleMQTransformer()
			val json = exampleRabbitMQTest.jsonStringFromMq.toCharArray.map(_.toByte)
			val api = new CompetitionLabsApiTest()
			api.createMember("109172", "bob", Seq("vip"))
			api.createEventAction("bet")
			api.createProduct("490", "My Reels", Seq("Acme co."), "slot", 1)

			When("the message is forwarded")
			transformer.apply(json, api)

			Then("the event should be received")
			assert(api.eventsReceivedForTest.keySet.contains("490"))
		}
	}
}

object exampleRabbitMQTest {
	val jsonStringFromMq: String =
		"""{
		  	"transaction": {
		  		"action": "bet",
		  		"transaction_id": "oahsdx123",
		  		"timestamp": "2017-07-30T17:20:20+00:00",
		  		"player_id": "109172",
		  		"game_type": 2,
		  		"game_id": 490,
		  		"skin_id": 4192,
		  		"bet_cents": 100,
		  		"round_id": "100",
		  		"win_cents": 0,
		  		"freespins_flag": false
		  	}
		  }"""
}

