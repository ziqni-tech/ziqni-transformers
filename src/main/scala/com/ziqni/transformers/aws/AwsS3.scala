package com.ziqni.transformers.aws

import awscala._
import awscala.s3._
import com.amazonaws.services.s3.model.{S3Object, S3ObjectSummary}
import scala.jdk.CollectionConverters.CollectionHasAsScala

case class AwsS3(ziqniAwsCredentials: AwsCredentials, bucketName: String, region: String) {

  // Implicit region declaration
  implicit val regionImpl: Region = Region.apply(region)
  private val s3Client: S3 = S3.apply(ziqniAwsCredentials.accessKeyId, ziqniAwsCredentials.secretAccessKey)(regionImpl)

  def listObjects(f :Iterable[S3ObjectSummary] => Unit): Unit  = {
    val listObjectsResponse = s3Client.listObjectsV2(bucketName)
    val seq = listObjectsResponse.getObjectSummaries.asScala
    f.apply(seq)
  }

  /**
   * List all the objects in the bucket
   * @param prefix Like folderA or FolderA/folderB
   * @return
   */
  def listObjectSubFolders(prefix:String, f :Iterable[S3ObjectSummary] => Unit): Unit  = {
    val p = {
      if(prefix.endsWith("/")) prefix
      else prefix+"/"
    }

    val listObjectsResponse = s3Client.listObjectsV2(bucketName,p)
    val seq = listObjectsResponse.getObjectSummaries.asScala
    f.apply(seq)
  }

  // Get an object from the bucket
  def getObject(key: String, f : S3Object => Unit): Unit  = {
    val s = s3Client.getObject(bucketName, key)
    f.apply(s)
  }

  // Delete an object from the bucket
  def deleteObject(key: String): Unit  = {
    s3Client.deleteObject(bucketName, key)
  }
}
