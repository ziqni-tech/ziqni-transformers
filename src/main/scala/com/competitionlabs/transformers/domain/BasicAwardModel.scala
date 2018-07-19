/***
  *  Copyright (C) Competition Labs Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */

package com.competitionlabs.transformers.domain

abstract class BasicAwardModel {

	/**
	  *
	  * @return Entity this Reward is linked to
	  */
	def getEntityId: String

	/**
	  *
	  * @return MemberId for the issued reward
	  */
	def getMemberId: String

	/**
	  *
	  * @return A flag to determine whether award has been claimed by member or not
	  */
	def claimed: Boolean

	/**
	  *
	  * @return CL Award id
	  */
	def getClAwardId: String

	/**
	  *
	  * @return Rank for the reward
	  */
	def getRewardRank: String

	/**
	  *
	  * @return Name of the Reward
	  */
	def getRewardName: String

	/**
	  *
	  * @return Value of the Reward
	  */
	def getRewardValue: Double

	/**
	  *
	  * @return Reward type key
	  */
	def getRewardTypeKey: String

	/**
	  *
	  * @return Description of the Reward
	  */
	def getRewardDescription: String

	/**
	  *
	  * @return CL Reward type id
	  */
	def getRewardTypeId: String

	/**
	  *
	  * @return CL Reward Id
	  */
	def getRewardId: String

	/**
	  *
	  * @return Key value pair of metadata information
	  */
	def getRewardMetaData: Option[Map[String, String]]

}