/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */

package com.ziqni.transformers.domain

abstract class ZiqniUnitOfMeasure {

	def getUnitOfMeasureKey: String

	/**
	  *
	  * @return Name of this UoM
	  */
	def getName: String

	/**
	  *
	  * @return IsoCode for the UoM
	  */
	def getIsoCode: Option[String]

	/**
	  *
	  * @return CL UoM id
	  */
	def getUnitOfMeasureId: String

	/**
	  *
	  * @return Multiplie for the UoM
	  */
	def getMultiplier: Double

	/**
	  *
	  * @return Type of UoM
	  */
	def getUnitOfMeasureType: String

	/**
	  *
	  * @return Key value pair of metadata information
	  */
	def getMetaData: Option[Map[String, String]]

}