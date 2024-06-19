package com.ziqni.transformers.aws

import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{DeleteObjectRequest, DeleteObjectResponse, GetObjectRequest, GetObjectResponse, ListObjectsV2Request, S3Object}
import software.amazon.awssdk.regions.Region

import scala.collection.mutable
import scala.jdk.CollectionConverters._

case class ZiqniAwsS3(ziqniAwsCredentials: ZiqniAwsCredentials, bucketName: String, region: String) {

  private val s3Client = S3Client.builder()
    .region(Region.of(region))
    .credentialsProvider(ziqniAwsCredentials.awsStaticCredentialsProvider)
    .build()

  def close(): Unit = {
    try {
      s3Client.close()
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def list(): mutable.Seq[S3Object] = {
    val listObjectsRequest = ListObjectsV2Request.builder()
      .bucket(bucketName)
      .build()

    val listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest)
    listObjectsResponse.contents().asScala
  }

  /**
   * List all the objects in the bucket
   * @param prefix Like folderA or FolderA/folderB
   * @return
   */
  def listSubFolders(prefix:String): mutable.Seq[S3Object] = {
    val p = {
      if(prefix.endsWith("/")) prefix
      else prefix+"/"
    }
    val listObjectsRequest = ListObjectsV2Request.builder()
      .bucket(bucketName)
      .prefix(p)
      .build()

    val listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest)
    listObjectsResponse.contents().asScala
  }

  // Get an object from the bucket
  def getObject(key: String): ResponseInputStream[GetObjectResponse] = {
    val getObjectRequest = GetObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .build()

    s3Client.getObject(getObjectRequest)
  }

  // Delete an object from the bucket
  def delete(key: String): DeleteObjectResponse = {
    val deleteObjectRequest = DeleteObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .build()

    s3Client.deleteObject(deleteObjectRequest)
  }
}
