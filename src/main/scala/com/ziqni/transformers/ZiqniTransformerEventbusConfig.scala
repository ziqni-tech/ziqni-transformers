package com.ziqni.transformers

/**
  *
  * @param address The address for this transformer
  * @param group The group this transformer belongs to
  * @param onMessageConsumers The message consumers
  */
case class ZiqniTransformerEventbusConfig(address:EventbusAddress, group:EventbusGroup, onMessageConsumers:List[EventbusConsumer])
