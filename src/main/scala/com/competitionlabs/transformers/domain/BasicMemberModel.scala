/***
  *  Copyright (C) Competition Labs Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */

package com.competitionlabs.transformers.domain

abstract class BasicMemberModel {

	/**
	  *
	  * @return Member Ref Id of remote system
	  */
	def getMemberRefId: String

	/**
	  *
	  * @return Display name as in remote system or default to ref id
	  */
	def getDisplayName: Option[String]

	/**
	  *
	  * @return List of groups this member belongs to
	  */
	def getGroups: Option[Array[String]]


	/**
	  *
	  * @return Any metadata associated with member
	  */
	def getMetaData: Option[Map[String, String]]

	/**
	  *
	  * @return CL Member id
	  */
	def getClMemberId: String

}