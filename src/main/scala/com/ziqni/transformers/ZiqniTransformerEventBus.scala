package com.ziqni.transformers

/**
  * The transformer event-bus is a light-weight distributed messaging system which allows different parts of your transformers
  * to communicate with each in a loosely coupled way.
  * An event-bus supports publish-subscribe messaging, point-to-point messaging and request-response messaging.
  * Message delivery is best-effort and messages can be lost if failure of all or part of the event bus occurs.
  */
trait ZiqniTransformerEventBus {

  type Address = String
  type Message = Map[String, Any]
  type Args = Map[String,String]
  type Consumer = (Option[Address], Message, Args) => Unit

  def registerConsumer(address: Address, onMessage: Consumer): Unit

  def sendToAddress(address: Address, message: Message, args: Option[Args]): Unit

  def sendToAll(message: Message, args: Option[Args]): Unit
}
