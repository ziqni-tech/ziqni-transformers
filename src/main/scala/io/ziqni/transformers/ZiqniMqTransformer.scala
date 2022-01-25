/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package io.ziqni.transformers

trait ZiqniMqTransformer {

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The message
	  * @param ziqniApi The Ziqni API
	  */
	def apply(message: Array[Byte], ziqniApi: ZiqniApi, args: Map[String,Any]): Unit
	
	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The message
	  * @param routingKey The routing key for the incoming message from the Envelope of AMQP client
	  * @param exchangeName The exchange name for the incoming message from the Envelope of AMQP client
	  * @param ziqniApi The Ziqni API
	  */
	def rabbit(message: Array[Byte], routingKey: String, exchangeName: String, ziqniApi: ZiqniApi): Unit =
		apply(
			message, ziqniApi, Map( "routingKey" -> routingKey, "exchangeName" -> exchangeName)
		)

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The value for the incoming message from the Kafka broker
	  * @param key The key for the incoming message from the Kafka broker
	  * @param ziqniApi The Ziqni API
	  */
	def kafka(key: Array[Byte], message: Array[Byte], ziqniApi: ZiqniApi): Unit =
		apply(
			message, ziqniApi, Map( "key" -> key )
		)

	/**
	  * This method gets executed when a message is received as a POST on API
	  * @param message The value for the incoming message from the http
	  * @param headers Header values for the incoming message
	  * @param ziqniApi The Ziqni API
	  */
	def http(headers: Map[String, Seq[String]], message: Array[Byte], ziqniApi: ZiqniApi): Unit =
		apply(
			message, ziqniApi, headers
		)

	/**
	  * This method gets executed when a message is received from an SQS endpoint
	  * @param headers Message headers
	  * @param message The message
	  * @param messageId Message id
	  * @param ziqniApi - The Ziqni API
	  */
	def sqs(headers: Map[String, String], message: Array[Byte], messageId: String, ziqniApi: ZiqniApi): Unit =
		apply(
			message, ziqniApi, headers + ("messageId" -> messageId)
		)
}
