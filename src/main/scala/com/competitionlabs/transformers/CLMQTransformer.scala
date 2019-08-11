/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2019
  */
package com.competitionlabs.transformers

trait CLMQTransformer {

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The message
	  * @param competitionLabsApi The CompetitionLabs API
	  */
	def apply(message: Array[Byte], competitionLabsApi: CompetitionLabsApi, args: Map[String,Any]): Unit
	
	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The message
	  * @param routingKey The routing key for the incoming message from the Envelope of AMQP client
	  * @param exchangeName The exchange name for the incoming message from the Envelope of AMQP client
	  * @param competitionLabsApi The CompetitionLabs API
	  */
	def rabbit(message: Array[Byte], routingKey: String, exchangeName: String, competitionLabsApi: CompetitionLabsApi): Unit =
		apply(
			message,
			competitionLabsApi,
			Map(
				"routingKey" -> routingKey,
				"exchangeName" -> exchangeName
			)
		)

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The value for the incoming message from the Kafka broker
	  * @param key The key for the incoming message from the Kafka broker
	  * @param competitionLabsApi The CompetitionLabs API
	  */
	def kafka(key: Array[Byte], message: Array[Byte], competitionLabsApi: CompetitionLabsApi): Unit =
		apply(
			message,
			competitionLabsApi,
			Map(
				"key" -> key
			)
		)

	/**
	  * This method gets executed when a message is received as a POST on API
	  * @param message The value for the incoming message from the http
	  * @param headers Header values for the incoming message
	  * @param competitionLabsApi The CompetitionLabs API
	  */
	def http(headers: Map[String, Seq[String]], message: Array[Byte], competitionLabsApi: CompetitionLabsApi): Unit =
		apply(
			message,
			competitionLabsApi,
			headers
		)

	/**
	  * This method gets executed when a message is received from an SQS endpoint
	  * @param headers Message headers
	  * @param message The message
	  * @param messageId Message id
	  * @param competitionLabsApi - The CompetitionLabs API
	  */
	def sqs(headers: Map[String, String], message: Array[Byte], messageId: String, competitionLabsApi: CompetitionLabsApi): Unit =
		apply(
			message,
			competitionLabsApi,
			headers + ("messageId" -> messageId)
		)
}
