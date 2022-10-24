package com.ziqni.transformers.domain

/**
  * @param callback The callback operation you are subscribing too. Like EntityChanged or EntityStateChanged
  * @param entityType What you are subscribing to, like Competition, Contest, Member, Award etc.
  */
case class BasicEntityChangeSubscriptionRequest(callback: String, entityType: String)
