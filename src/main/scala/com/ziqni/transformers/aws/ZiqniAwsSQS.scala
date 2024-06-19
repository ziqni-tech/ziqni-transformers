package com.ziqni.transformers.aws

import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import software.amazon.awssdk.services.sqs.SqsClient

case class ZiqniAwsSQS(ziqniAwsCredentials: ZiqniAwsCredentials, region: String, queueUrl: String) {

  private val client:SqsClient = SqsClient.builder()
    .credentialsProvider(ziqniAwsCredentials.awsStaticCredentialsProvider)
    .region(software.amazon.awssdk.regions.Region.of(region))
    .build()

  def close(): Unit = {
    try {
      client.close()
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def sendMessage(messageBody: String, messageGroupId: String = "ziqni-prod", messageDeduplicationId: String = "1"): Unit = {
    val sendMsgRequest: SendMessageRequest.Builder = SendMessageRequest.builder()
      .queueUrl(queueUrl)
      .messageBody(messageBody)
      .messageGroupId(messageGroupId) // required for FIFO queues
      .messageDeduplicationId(messageDeduplicationId) // required unless the queue is configured to generate deduplication IDs automatically

    client.sendMessage(sendMsgRequest.build())
  }

}
