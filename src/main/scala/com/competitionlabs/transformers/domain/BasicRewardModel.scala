/***
  *  Copyright (C) Competition Labs Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */

package com.competitionlabs.transformers.domain

case class BasicRewardModel (
								clRewardId: String,
								rewardName: String,
								rewardValue: Double,
								rewardTypeId: String,
								rewardTypeName: String,
								rewardTypeKey: String
							)
