package com.ziqni.transformers.domain

case class GetOrCreateResult[T](result:T, created:Boolean)
