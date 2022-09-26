/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers

trait ZiqniMqTransformer {

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The message
	  * @param ziqniContext The Ziqni Context
	  */
	def apply(message: Array[Byte], ziqniContext: ZiqniContext, args: Map[String,Any]): Unit

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The message
	  * @param routingKey The routing key for the incoming message from the Envelope of AMQP client
	  * @param exchangeName The exchange name for the incoming message from the Envelope of AMQP client
	  * @param ziqniContext The Ziqni Context
	  */
	def rabbit(message: Array[Byte], routingKey: String, exchangeName: String, ziqniContext: ZiqniContext): Unit =
		apply(
			message, ziqniContext, Map( "routingKey" -> routingKey, "exchangeName" -> exchangeName)
		)

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The value for the incoming message from the Kafka broker
	  * @param key The key for the incoming message from the Kafka broker
	  * @param ziqniContext The Ziqni Context
	  */
	def kafka(key: Array[Byte], message: Array[Byte], ziqniContext: ZiqniContext): Unit =
		apply(
			message, ziqniContext, Map( "key" -> key )
		)

	/**
	  * This method gets executed when a message is received as a POST on API
	  * @param message The value for the incoming message from the http
	  * @param headers Header values for the incoming message
	  * @param ziqniContext The Ziqni Context
	  */
	def http(headers: Map[String, Seq[String]], message: Array[Byte], ziqniContext: ZiqniContext): Unit =
		apply(
			message, ziqniContext, headers
		)

	/**
	  * This method gets executed when a message is received from an SQS endpoint
	  * @param headers Message headers
	  * @param message The message
	  * @param messageId Message id
	  * @param ziqniContext - The Ziqni Context
	  */
	def sqs(headers: Map[String, String], message: Array[Byte], messageId: String, ziqniContext: ZiqniContext): Unit =
		apply(
			message, ziqniContext, headers + ("messageId" -> messageId)
		)

	/**
		* Happens when the class is initialized
		*/
	def init(ziqniTransformerInfo:ZiqniTransformerInfo, initEventBus: (EventbusAddress, EventbusGroup, List[EventbusConsumer]) => ZiqniTransformerEventBus): Option[ZiqniTransformerEventBus] = None
}
