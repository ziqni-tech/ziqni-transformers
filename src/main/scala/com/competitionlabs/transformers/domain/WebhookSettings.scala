/***
  *  Copyright (C) Competition Labs Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2019
  */

package com.competitionlabs.transformers.domain

abstract class WebhookSettings {

	def url: String

	def headers: Map[String, Seq[String]]
}
