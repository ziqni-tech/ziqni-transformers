/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */

package com.ziqni.transformers.domain

import org.joda.time.DateTime

abstract class ZiqniContest {

	/**
	  *
	  * @return Get the name of Contest
	  */
	def getName: String

	/**
	  *
	  * @return Get the description of Contest
	  */
	def getCompetitionId: String

	/**
	  *
	  * @return Get start date of Contest
	  */
	def getStartDate: DateTime

	/**
	  *
	  * @return Get end date of Contest
	  */
	def getEndDate: Option[DateTime]

	/**
	  *
	  * @return Get created time of Contest
	  */
	def getCreatedTime: DateTime

	/**
	  *
	  * @return CL id for Contest
	  */
	def getContestId: String

	/**
	  *
	  * @return Product reference ids used in rules for this Contest
	  */
	def getProductRefIds: Option[Array[String]]

	/**
	  *
	  * @return List of member groups that can participate in this competition
	  */
	def getGroups: Option[Array[String]]

	/**
	  *
	  * @return Returns the status of Contest
	  */
	def getStatus: String

	/**
	  *
	  * @return Key value pair of metadata information
	  */
	def getMetaData: Option[Map[String, String]]

	/**
	  * Update metadata of the Contest
	  * @param metadata Key value pair of information
	  */
	def setMetaData(metadata: Map[String, String]): Unit

	/**
	  * System will update the status
	  */
	def setStatus(): Unit

	/**
		* Get the custom fields
		*
		* @return key value pair map of custom field entries
		*/
	def getCustomFields: Map[String, CustomFieldEntry[_ <: Any]]
}
