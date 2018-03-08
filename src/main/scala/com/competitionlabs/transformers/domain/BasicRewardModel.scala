/***
  *  Copyright (C) Competition Labs Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */

package com.competitionlabs.transformers.domain

//case class BasicRewardModel (
//								clRewardId: String,
//								rewardName: String,
//								rewardValue: Double,
//								rewardTypeId: String,
//								rewardTypeName: String,
//								rewardTypeKey: String
//							)

abstract class BasicRewardModel {

	/**
	  *
	  * @return Entity this Reward is linked to
	  */
	def getEntityId: String

	/**
	  *
	  * @return Rank for the reward
	  */
	def getRank: String

	/**
	  *
	  * @return Name of the Reward
	  */
	def getName: String

	/**
	  *
	  * @return Description of the Reward
	  */
	def getDescription: String

	/**
	  *
	  * @return Delay for Reward
	  */
	def getDelay: Int

	/**
	  *
	  * @return Value of the Reward
	  */
	def getValue: Double

	/**
	  *
	  * @return CL Reward type id
	  */
	def getRewardTypeId: String

	/**
	  *
	  * @return Reward type name
	  */
	def getRewardTypeName: String

	/**
	  *
	  * @return Reward type key
	  */
	def getRewardTypeKey: String

	/**
	  *
	  * @return CL Reward id
	  */
	def getClRewardId: String

}