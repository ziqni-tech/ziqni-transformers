/***
  *  Copyright (C) Competition Labs Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */

package com.competitionlabs.transformers.domain

import org.joda.time.DateTime

abstract class BasicContestModel {

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
	def getClContestId: String

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
}
