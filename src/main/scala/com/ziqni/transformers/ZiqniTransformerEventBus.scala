package com.ziqni.transformers

/**
  * The transformer event-bus is a light-weight distributed messaging system which allows different parts of your transformers
  * to communicate with each in a loosely coupled way.
  * An event-bus supports publish-subscribe messaging, point-to-point messaging and request-response messaging.
  * Message delivery is best-effort and messages can be lost if failure of all or part of the event bus occurs.
  */
trait ZiqniTransformerEventBus {

  def getTransformerInfo: ZiqniTransformerInfo

  def getAddress: EventbusAddress

  def getGroup: String

  def sendToAddress(toAddress: EventbusAddress, message: EventbusMessage, args: Option[EventbusArgs]): Unit

  def sendToGroup(group: EventbusGroup, message: EventbusMessage, args: Option[EventbusArgs]): Unit

  def sendToAll(message: EventbusMessage, args: Option[EventbusArgs]): Unit
}
