package com.competitionlabs.transformers.domain

abstract class WebhookSettings {

	def url: String

	def headers: Map[String, Seq[String]]
}
