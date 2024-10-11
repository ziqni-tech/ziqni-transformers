package com.ziqni.transformers.domain

case class ZiqniQueryResult[T](totalRecords: Long, resultCount: Long, limit: Long, skip: Long, results: Seq[_<:T])
