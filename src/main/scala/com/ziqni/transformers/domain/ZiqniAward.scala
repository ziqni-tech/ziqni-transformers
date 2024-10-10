/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */

package com.ziqni.transformers.domain

abstract class ZiqniAward {

	/**
	  *
	  * @return Entity this Reward is linked to
	  */
	def getEntityId: String

	/**
	  *
	  * @return Entity type this Award is linked to
	  */
	def getEntityType: String

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
	  * @return ZIQNI Award id
	  */
	def getAwardId: String

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
	  * @return ZIQNI Reward type id
	  */
	def getRewardTypeId: String

	/**
	  *
	  * @return ZIQNI Reward Id
	  */
	def getRewardId: String

	/**
	  *
	  * @return Key value pair of metadata information
	  */
	def getRewardMetaData: Option[Map[String, String]]

	/**
	 * Get the status code of the response
	 * @return The status code
	 */
	def getStatusCode: Int

	/**
	 * The is active from this epoch date time in seconds
	 * @return The active from date
	 */
	def getActiveFrom: Long

	/**
	 * The is active until this epoch date time in seconds
	 * @return The active until date
	 */
	def getActiveUntil: Long
}
