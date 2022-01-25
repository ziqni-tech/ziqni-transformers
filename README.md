# Ziqni Transformer Documentation

## What are the Ziqni Transformers

Ziqni Transformers are a way to integrate with the Ziqni platform without having to make modifications to your message formats on your back-end systems. Using the Ziqni Transformers, you can send a message to Ziqni in your current format, transform the message into a form that can be accepted by the Ziqni API, and push that message to Ziqni. You can also transform messages before they are POSTed to your system via the Ziqni WebHooks.

Ziqni Transformers are created by writing small Scala classes that extend the Ziqni Transformer classes to process messages.

Inbound events sent via RabbitMQ are transformed by extending the `ZqMqTransformer` class. Your transformation class is run by the Ziqni platform when the message is received.

Outbound events sent via WebHooks are transformed by extending the `ZqWebhookTransformer` class. Your transformation class is run by the Ziqni platform when an action occurs that would trigger a WebHook to fire.

|  If you want to | Write a class that extends |
|---|---|
| Map your existing MQ messages to a Ziqni Event  | `ZqMqTransformer`  |
| Map a Ziqni WebHook message to a format your back-end systems accept | `ZqWebhookTransformer`  |
|    |   |


## Common use cases for Ziqni Transformers

Beyond mapping of message fields from your platform to Ziqni, below are some common uses for Ziqni Transformers:

* Create a new member when Ziqni receives an event from a sign-up event
* Push a message into your platform when a new Product is created within Ziqni
* Retrieve customer segment information from your platform for a new member

## Transform incoming messages from a RabbitMQ Queue

Ziqni MQ transformer is used to transform a message received via RabbitMQ to your Ziqni space. Within the transformer, you can map the fields in an incoming message to the Ziqni API, trigger addition events, or implement custom business login.

To transform an incoming message from a RabbitMQ Queue:

1. Create a class that extends the `ZqMqTransformer` class

   ```scala
   package example.transformers

   import io.ziqni.transformers.{ZqMqTransformer, ZiqniApi}

   class exampleMQTranformer extends ZqMqTransformer {
     /**
       * This method gets executed when a message is received on the message queue
       *
       * @param message            The message
       * @param ZiqniApi The Ziqni API
       */
     override def apply(message: Array[Byte], ZiqniApi: ZiqniApi): Unit = {
         // Write your code to transform the message here
     }
   }
   ```

2. Test your class using Scala Test and the `ZiqniApiTest` module

	```scala
	import example.transformers.exampleMQTranformer
	import org.scalatest._
	import utils.ZiqniApiTest

	class exampleMQTranformer extends FunSpec with Matchers with GivenWhenThen with BeforeAndAfterEach with BeforeAndAfterAll {

	describe("Test the EXAMPLE message queue receiver implementation") {

		//  Run the test
		it("should receive a published a message and transform it into a Ziqni event") {

			// Prepare the test
			val eventReferenceId = "1234"
			val transformer = new exampleMQTranformer()
			val json = exampleRabbitMQTest.jsonStringFromMq.toCharArray.map(_.toByte)
			val api = new ZiqniApiTest()
			api.createMember("109172","bob", Seq("vip"))
			api.createEventAction("bet")
			api.createProduct("490","Scooby Slots", Seq("EX"),"slot", 1)

			When("the message is forwarded")
			transformer.apply(json, api)

			Then("the event should be received")
			assert(api.eventsReceivedForTest.keySet.contains(eventReferenceId))
		}

		it("should receive a published a message create the member and transform it into a Ziqni event") {

			// Prepare the test
			val eventReferenceId = "1234"
			val transformer = new exampleMQTransformer()
			val json = exampleRabbitMQTest.jsonStringFromMq.toCharArray.map(_.toByte)
			val api = new ZiqniApiTest()
			api.createEventAction("bet")
			api.createProduct("490","Scooby Slots", Seq("EX"),"slot", 1)

			When("the message is forwarded")
			transformer.apply(json, api)

			Then("the event should be received")
			assert(api.eventsReceivedForTest.keySet.contains(eventReferenceId))
		}



		it("should receive a published a message create the action and transform it into a Ziqni event") {

			// Prepare the test
			val eventReferenceId = "1234"
			val transformer = new exampleMQTransformer()
			val json = exampleRabbitMQTest.jsonStringFromMq.toCharArray.map(_.toByte)
			val api = new ZiqniApiTest()
			api.createMember("109172","bob", Seq("vip"))
			api.createProduct("490","Scooby Slots", Seq("EX"),"slot", 1)

			When("the message is forwarded")
			transformer.apply(json, api)

			Then("the event should be received")
			assert(api.eventsReceivedForTest.keySet.contains(eventReferenceId))
		}



		it("should receive a published a message create the product and transform it into a Ziqni event") {

			// Prepare the test
			val eventReferenceId = "1234"
			val transformer = new exampleBetMQTransformer()
			val json = exampleRabbitMQTest.jsonStringFromMq.toCharArray.map(_.toByte)
			val api = new ZiqniApiTest()
			api.createMember("109172","bob", Seq("vip"))
			api.createEventAction("bet")

			When("the message is forwarded")
			transformer.apply(json, api)

			Then("the event should be received")
			assert(api.eventsReceivedForTest.keySet.contains(eventReferenceId))
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
			  		"win_cents": 0,
			  		"currency": "USD",
			  		"symbols": "11-15-13-1-7-11-12-11-6-6-8-9-10-6-9-4-2-16-9-7-8-14-11-1-6",
		  			"freespins_flag": false,
			  		"freerounds_flag": false
			  	}
			  }"""
		}
		```


## Transform outgoing WebHook messages from Ziqni

You can use Ziqni WebHook transformers to modify and send messages to your WebHook endpoints. You can map field in the Ziqni data model to fields already implemented in your platform. You can also use the Ziqni Transformer API to trigger additional actions and events.

You can create a Ziqni WebHook transformer for any of the built in WebHook events. If you have a custom WebHook transformer configured, your custom transformer is used instead of the built in WebHook transformer.

To create a Ziqni WebHook transformer:

1. Create a new class that extends ZqWebhookTransformer.

   ```scala
   package example.transformers

   import io.ziqni.transformers.{ZqWebhookTransformer, ZiqniApiExt}
   import io.ziqni.transformers.domain.WebhookSettings

   class exampleWebHookTransformer extends ZqWebhookTransformer {

   // /**
   // * Executed when a new member is registered in your Ziqni space
   // * @param settings The user supplied settings
   // * @param memberId The Ziqni member id
   // * @param ZiqniApi The Ziqni API
   // */
   // override def onNewMember(settings: WebhookSettings, memberId: String, ZiqniApi: ZiqniApiExt): Unit = {
       }
   }
   ```

2. Override one of the methods in ZqWebhookTransformer with your implementation

	The following are the built in WebHook transformers you can override with your own custom transformer:

	* onNewProduct(): Executed when a new product is registered in your Ziqni space
    * onNewMember(): Executed when a new member is registered in your Ziqni space
    * onCompetitionCreated(): Executed when a new competition is created in your Ziqni space
    * onCompetitionStarted(): Executed when a competition is started
    * onCompetitionFinished(): Executed when a competition finished
    * onCompetitionCancelled(): Executed when a competition is cancelled
    * onCompetitionRewardIssued(): Executed when a reward for a competition is issued
    * onContestCreated(): Executed when a new contest is created in your Ziqni space
    * onContestStarted(): Executed when a contest is started
    * onContestFinished(): Executed when a contest finished
    * onContestFinalised(): Executed when a contest is finalised
    * onContestCancelled(): Executed when a contest is cancelled
    * onContestRewardCreated(): Executed when a contest reward is created
    * onContestRewardIssued(): Executed when a contest reward is issued
    * onAchievementCreated(): Executed when a new achievement is created
    * onAchievementTriggered(): Executed when an achievement is triggered
    * onAchievementRewardCreated(): Executed when an achievement reward is created
    * onAchievementRewardIssued(): Executed when a reward was awarded to a member


### Example WebHook transformer

Here is an example of a WebHook transformer.

```scala

import io.ziqni.transformers.domain.WebhookSettings
import io.ziqni.transformers.{ZqWebhookTransformer, ZiqniApiExt}
import org.joda.time.DateTime

class DefaultWebhookTransformer extends ZqWebhookTransformer {
		override def onAchievementTriggered(settings: WebhookSettings, achievementId: String, memberId: String, ZiqniApi: ZiqniApiExt): Unit = {

		val body = Map[String, Any](
			"accountId" -> ZiqniApi.accountId,
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberIdRefId" -> ZiqniApi.memberRefIdFromMemberId(memberId),
			"resourcePath" -> s"/api/${ZiqniApi.spaceName}/achievement/$achievementId",
			"timestamp" -> DateTime.now().getMillis
		)

		val json =  ZiqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ZiqniApi.HTTPDefaultHeader

		ZiqniApi.httpPost(settings.url, json, headers)
	}
}

```


## Developing and Testing Transformers

This section covers the tools needed to begin writing and testing Ziqni Transformers

### System Requirements

These are the tools you will need to develop Ziqni Transformers

Scala 2.13.7
Java 11 JDK
JetBrains IntelliJ (Optional)

### Ziqni Transformer API

The Ziqni Transformer API is used within your transformer classes to perform various operations like looking up members, pushing events, creating actions, etc.

You can view the methods available for ZqMqTransformer and ZqWebhookTransformer classes here:
`Ziqni-transformers/src/main/scala/com/Ziqni/transformers/ZiqniApi.scala`

You can view addition methods available for ZqWebhookTransformer classes here:
`clabs/Ziqni-transformers/src/main/scala/com/Ziqni/transformers/ZiqniApiExt.scala`
