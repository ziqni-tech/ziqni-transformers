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
	  * @param ziqniApi The Ziqni API
	  */
	def apply(message: Array[Byte], ziqniApi: ZiqniApi, transformerEventBus:Option[ZiqniTransformerEventBus], args: Map[String,Any]): Unit = {
		apply(message,ziqniApi,args)
	}

	def apply(message: Array[Byte], ziqniApi: ZiqniApi, args: Map[String,Any]): Unit

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The message
	  * @param routingKey The routing key for the incoming message from the Envelope of AMQP client
	  * @param exchangeName The exchange name for the incoming message from the Envelope of AMQP client
	  * @param ziqniApi The Ziqni API
	  */
	def rabbit(message: Array[Byte], routingKey: String, exchangeName: String, ziqniApi: ZiqniApi): Unit = rabbit(message,routingKey,exchangeName,ziqniApi,None)

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The message
	  * @param routingKey The routing key for the incoming message from the Envelope of AMQP client
	  * @param exchangeName The exchange name for the incoming message from the Envelope of AMQP client
	  * @param ziqniApi The Ziqni API
	  */
	def rabbit(message: Array[Byte], routingKey: String, exchangeName: String, ziqniApi: ZiqniApi, transformerEventBus:Option[ZiqniTransformerEventBus]): Unit =
		apply(
			message, ziqniApi, transformerEventBus , Map( "routingKey" -> routingKey, "exchangeName" -> exchangeName)
		)

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The value for the incoming message from the Kafka broker
	  * @param key The key for the incoming message from the Kafka broker
	  * @param ziqniApi The Ziqni API
	  */
	def kafka(key: Array[Byte], message: Array[Byte], ziqniApi: ZiqniApi): Unit = kafka(key,message,ziqniApi,None)

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The value for the incoming message from the Kafka broker
	  * @param key The key for the incoming message from the Kafka broker
	  * @param ziqniApi The Ziqni API
	  */
	def kafka(key: Array[Byte], message: Array[Byte], ziqniApi: ZiqniApi, transformerEventBus:Option[ZiqniTransformerEventBus]): Unit =
		apply(
			message, ziqniApi, transformerEventBus, Map( "key" -> key )
		)

	/**
	  * This method gets executed when a message is received as a POST on API
	  * @param message The value for the incoming message from the http
	  * @param headers Header values for the incoming message
	  * @param ziqniApi The Ziqni API
	  */
	def http(headers: Map[String, Seq[String]], message: Array[Byte], ziqniApi: ZiqniApi): Unit = http(headers,message,ziqniApi,None)

	/**
	  * This method gets executed when a message is received as a POST on API
	  * @param message The value for the incoming message from the http
	  * @param headers Header values for the incoming message
	  * @param ziqniApi The Ziqni API
	  */
	def http(headers: Map[String, Seq[String]], message: Array[Byte], ziqniApi: ZiqniApi, transformerEventBus:Option[ZiqniTransformerEventBus]): Unit =
		apply(
			message, ziqniApi, transformerEventBus, headers
		)

	/**
	  * This method gets executed when a message is received from an SQS endpoint
	  * @param headers Message headers
	  * @param message The message
	  * @param messageId Message id
	  * @param ziqniApi - The Ziqni API
	  */
	def sqs(headers: Map[String, String], message: Array[Byte], messageId: String, ziqniApi: ZiqniApi): Unit = sqs(headers,message,messageId,ziqniApi,None)

	/**
	  * This method gets executed when a message is received from an SQS endpoint
	  * @param headers Message headers
	  * @param message The message
	  * @param messageId Message id
	  * @param ziqniApi - The Ziqni API
	  */
	def sqs(headers: Map[String, String], message: Array[Byte], messageId: String, ziqniApi: ZiqniApi, transformerEventBus:Option[ZiqniTransformerEventBus]): Unit =
		apply(
			message, ziqniApi, transformerEventBus, headers + ("messageId" -> messageId)
		)

	/**
		* Happens when the class is initialized
		*/
	def init(ziqniApi: ZiqniApi, ziqniTransformerInfo:ZiqniTransformerInfo, initEventBus: (EventbusAddress, EventbusGroup, List[EventbusConsumer]) => ZiqniTransformerEventBus): Option[ZiqniTransformerEventBus] = None
}
