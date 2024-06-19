package com.ziqni.transformers.aws

import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{ListObjectsV2Request, S3Object}

import scala.collection.mutable
import scala.jdk.CollectionConverters._

case class ZiqniAwsS3(ziqniAwsCredentials: ZiqniAwsCredentials, bucketName: String, region: String) {

  val s3Client = S3Client.builder()
    .region(software.amazon.awssdk.regions.Region.of(region))
    .credentialsProvider(ziqniAwsCredentials.awsStaticCredentialsProvider)
    .build()

  def listObjectsInTheBucket(): mutable.Seq[S3Object] = {

    val listObjectsRequest = ListObjectsV2Request.builder()
      .bucket(bucketName)
      .build()

    val listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest)
    listObjectsResponse.contents().asScala
  }
}
