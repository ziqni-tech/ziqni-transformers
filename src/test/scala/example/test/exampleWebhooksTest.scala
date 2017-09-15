/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */
package example.test

import com.competitionlabs.transformers.domain.WebhookSettings
import example.transformers.exampleOnNewProductWebhook
import org.scalatest._
import utils.CompetitionLabsApiTest

class exampleWebhooksTest extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfterEach with BeforeAndAfterAll {

	describe("Test the webhook implementation") {
		
		it("should receive a published a message and transform it into a CompetitionLabs event") {

			// Prepare the test
			val webhook = new exampleOnNewProductWebhook()
			val settings = new WebhookSettings() {
				override def url = "http://domain.local"
				override def headers = Map("test" -> Seq("true"))
			}
			val api = new CompetitionLabsApiTest()
			api.createMember("109172","bob", Seq("vip"))
			api.createEventAction("bet")
			val productId = api.createProduct("490","Neon Reels", Seq("ISB"),"slot", 1)

			When("the new product event is received")
			webhook.onNewProduct(settings, productId.get, api)

			Then("the event should be received")
			assert(api.httpRequests.keySet.contains(settings.url))
		}
	}
}

