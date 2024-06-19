package com.ziqni.transformers.aws

import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.{DeleteObjectRequest, DeleteObjectResponse, GetObjectRequest, GetObjectResponse, ListObjectsV2Request, S3Object}
import software.amazon.awssdk.regions.Region

import scala.collection.mutable
import scala.jdk.CollectionConverters._

case class AwsS3(ziqniAwsCredentials: AwsCredentials, bucketName: String, region: String) {

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

  def listObjects(f :mutable.Seq[S3Object] => Unit): Unit  = {
    val listObjectsRequest = ListObjectsV2Request.builder()
      .bucket(bucketName)
      .build()

    val listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest)
    val seq = listObjectsResponse.contents().asScala
    f.apply(seq)
  }

  /**
   * List all the objects in the bucket
   * @param prefix Like folderA or FolderA/folderB
   * @return
   */
  def listObjectSubFolders(prefix:String, f :mutable.Seq[S3Object] => Unit): Unit  = {
    val p = {
      if(prefix.endsWith("/")) prefix
      else prefix+"/"
    }
    val listObjectsRequest = ListObjectsV2Request.builder()
      .bucket(bucketName)
      .prefix(p)
      .build()

    val listObjectsResponse = s3Client.listObjectsV2(listObjectsRequest)
    val seq = listObjectsResponse.contents().asScala
    f.apply(seq)
  }

  // Get an object from the bucket
  def getObject(key: String, f :ResponseInputStream[GetObjectResponse] => Unit): Unit  = {
    val getObjectRequest = GetObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .build()

    val s = s3Client.getObject(getObjectRequest)
    f.apply(s)
  }

  // Delete an object from the bucket
  def deleteObject(key: String, f :DeleteObjectResponse => Unit): Unit  = {
    val deleteObjectRequest = DeleteObjectRequest.builder()
      .bucket(bucketName)
      .key(key)
      .build()

    val r = s3Client.deleteObject(deleteObjectRequest)
    f.apply(r)
  }
}
