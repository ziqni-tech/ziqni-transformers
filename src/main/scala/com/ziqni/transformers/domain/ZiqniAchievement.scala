/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */

package com.ziqni.transformers.domain

import org.joda.time.DateTime

abstract class ZiqniAchievement {

    /**
      *
      * @return Get the name of achievement
      */
    def getName: String

    /**
      *
      * @return Get the description of achievement
      */
    def getDescription: Option[String]

    /**
      *
      * @return check if achievement is deprecated or not
      */
    def isDeprecated: Boolean

    /**
      *
      * @return Get start date of achievement
      */
    def getStartDate: DateTime

    /**
      *
      * @return Get end date of achievement
      */
    def getEndDate: Option[DateTime]

    /**
      *
      * @return Get the category of achievement
      */
    def getCategory: Option[Array[String]]

    /**
      *
      * @return Get created time of achievement
      */
    def getCreatedTime: DateTime

    /**
      *
      * @return CL id for achievement
      */
    def getAchievementId: String

    /**
      *
      * @return Product reference ids used in rules for this achievement
      */
    def getProductRefIds: Option[Array[String]]

    /**
      *
      * @return List of member groups this achievement is available to
      */
    def getGroups: Option[Array[String]]

    /**
      *
      * @return Returns the status of Achievement
      */
    def getStatus: String

    /**
      *
      * @return Key value pair of metadata information
      */
    def getMetaData: Option[Map[String, String]]



    /**
      * Update metadata of the achievement
      * @param metadata Key value pair of information
      */
    def setMetaData(metadata: Map[String, String]): Unit

    /**
      * System will update the status
      */
    def setStatus(): Unit

    /**
      * Get the custom fields
      * @return key value pair map of custom field entries
      */
    def getCustomFields(): Map[String, CustomFieldEntry[_<:Any]]
}
