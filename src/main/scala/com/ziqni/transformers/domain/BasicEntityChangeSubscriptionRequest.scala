package com.ziqni.transformers.domain

/**
  *
  * @param action Either subscribe or unsubscribe from the data feed
  * @param callback The callback operation you are subscribing too. Like entityChanged or entityStateChanged
  * @param entityType What you are subscribing to, like Competition, Contest, Member, Award etc.
  */
case class BasicEntityChangeSubscriptionRequest(action: String, callback: String, entityType: String)
