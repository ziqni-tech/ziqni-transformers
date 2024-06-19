package com.ziqni.transformers.aws

import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}


/**
 * This is required to get around the, Dynamic Compilation Limitations
 * Scala's ToolBox is used for dynamically compiling and executing code. However, it sometimes struggles with Java interoperability, especially with complex or nested Java structures.
 * The ToolBox may not always correctly handle the classpath or dependencies required for these Java classes.
 **/
case class ZiqniAwsCredentials(accessKeyId:String, secretAccessKey:String) {

  val awsBasicCredentials: AwsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey)

  lazy val awsStaticCredentialsProvider: StaticCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials)
}
