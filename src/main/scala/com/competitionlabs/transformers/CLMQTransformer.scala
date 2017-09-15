/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */
package com.competitionlabs.transformers

trait CLMQTransformer {

	/**
	  * This method gets executed when a message is received on the message queue
	  * @param message The message
	  * @param competitionLabsApi The CompetitionLabs API
	  */
	def apply(message: Array[Byte], competitionLabsApi: CompetitionLabsApi): Unit
}
