/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */

package com.ziqni.transformers.domain

abstract class ZiqniMember {

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
	  * @return List of tags associated with the member
	  */
	def getTags: Option[Seq[String]]

	/**
	  *
	  * @return Any metadata associated with member
	  */
	def getMetaData: Option[Map[String, String]]

	/**
	  *
	  * @return ZIQNI Member id
	  */
	def getMemberId: String

	/**
		* Get the custom fields
		*
		* @return key value pair map of custom field entries
		*/
	def getCustomFields: Map[String, CustomFieldEntry[_ <: Any]]

}