package com.ziqni.transformers.domain

case class CreateProduct(productReferenceId: String, displayName: String, providers: Seq[String], productType: String, defaultAdjustmentFactor: Double, customFields: Map[String,CustomFieldEntry[_<:Any]], metaData: Option[Map[String, String]] = None) {

}
