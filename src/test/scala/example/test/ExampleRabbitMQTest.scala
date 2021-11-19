/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package example.test

import example.transformers.ExampleMqTransformer
import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import utils.ZiqniApiTest

class ExampleRabbitMQTest extends AnyFlatSpec with GivenWhenThen with BeforeAndAfterEach with BeforeAndAfterAll {



		//  Run the test
		it should "receive a published a message and transform it into a CompetitionLabs event" in {

			// Prepare the test
			val transformer = new ExampleMqTransformer()
			val json = ExampleRabbitMQTest.jsonStringFromMq.toCharArray.map(_.toByte)
			val api = new ZiqniApiTest()
			api.createMember("109172", "bob", Seq("vip"))
			api.createEventAction("bet")
			api.createProduct("490", "My Reels", Seq("Acme co."), "slot", 1)

			When("the message is forwarded")
			transformer.apply(json, api, Map.empty)

			Then("the event should be received")
			assert(api.eventsReceivedForTest.keySet.contains("490"))
		}

}

object ExampleRabbitMQTest {
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

