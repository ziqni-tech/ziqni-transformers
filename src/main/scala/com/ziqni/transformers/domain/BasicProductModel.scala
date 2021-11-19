/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */

package com.ziqni.transformers.domain

abstract class BasicProductModel {

	/**
	  *
	  * @return Product Ref Id of remote system
	  */
	def getProductReferenceId: String

	/**
	  *
	  * @return Name of the Product
	  */
	def getName: String

	/**
	  *
	  * @return List of providers this product belongs to
	  */
	def getProviders: Array[String]

	/**
	  *
	  * @return Type of product
	  */
	def getProductType: String

	/**
	  *
	  * @return Default adjustment factor
	  */
	def getDefaultAdjustmentFactor: Option[Double]

	/**
	  *
	  * @return Any metadata associated with product
	  */
	def getMetaData: Option[Map[String, String]]

	/**
	  *
	  * @return CL Product id
	  */
	def getClProductId: String

}