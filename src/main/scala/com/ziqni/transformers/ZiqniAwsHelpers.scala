package com.ziqni.transformers

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.{S3Client, S3ClientBuilder}
import software.amazon.awssdk.services.sqs.{SqsClient, SqsClientBuilder}
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

class ZiqniAwsHelpers {

  def createAwsBasicCredentials(accessKeyId:String,secretAccessKey:String): AwsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey)

  def getSqsClientBuilder: SqsClientBuilder = SqsClient.builder()

  def getSendMessageRequestBuilder = SendMessageRequest.builder()

  def getS3ClientBuilder: S3ClientBuilder = S3Client.builder

  def getSendMsgRequest: SendMessageRequest.Builder = SendMessageRequest.builder()

  def getObjectRequestBuilder: GetObjectRequest.Builder = GetObjectRequest.builder()
}
