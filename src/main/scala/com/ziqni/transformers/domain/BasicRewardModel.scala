/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */

package com.ziqni.transformers.domain

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
		* @return Entity type this Reward is linked to
		*/
	def getEntityType: String

	/**
	  *
	  * @return Reward type key
	  */
	def getRewardTypeKey: String

	/**
	  *
	  * @return Key value pair of metadata information
	  */
	def getMetaData: Option[Map[String, String]]

	/**
	  * Update metadata of the Reward
	  * @param metadata Key value pair of information
	  */
	def setMetaData(metadata: Map[String, String]): Unit

	/**
	  *
	  * @return CL Reward id
	  */
	def getClRewardId: String

	/**
		* Get the custom fields
		*
		* @return key value pair map of custom field entries
		*/
	def getCustomFields(): Map[String, CustomFieldEntry[_ <: Any]]

}