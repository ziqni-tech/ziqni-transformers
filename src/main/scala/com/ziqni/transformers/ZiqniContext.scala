package com.ziqni.transformers

import com.ziqni.transformers.ZiqniContext.SpaceName

import scala.concurrent.ExecutionContextExecutor
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jackson.Serialization
import org.json4s.{DefaultFormats, JValue}
import com.ziqni.transformers.domain._
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, AwsCredentialsProvider, StaticCredentialsProvider}

import java.nio.charset.Charset

object ZiqniContext {

  type SpaceName = String;

  /**
    * Converts byte array to String using UTF-8
    *
    * @param body    The string encoded as bytes
    * @param charset Optional, character set to decode byte array, default is UTF-8
    * @return Decoded string or throws exception
    */
  def convertByteArrayToString(body: Array[Byte], charset: String = "UTF-8"): String = new String(body, Charset.forName(charset))

  /**
    * Converts a json string to a JValue
    *
    * @param body The string to deserialise
    * @return JValue or throws exception
    */
  def fromJsonString(body: String): JValue = parse(body,false,true)

  /**
    * Converts a map to a json string
    *
    * @param obj The object to serialise
    * @return json string or throws exception
    */
  def toJsonFromMap(obj: Map[String, Any]): String = Serialization.write(obj)(DefaultFormats)
}

trait ZiqniContext {
  def accountId: String

  def spaceName: String

  def ziqniTransformerInfo: ZiqniTransformerInfo

  def ziqniApi: ZiqniApi

  def ziqniApiAsync: ZiqniApiAsync

  def ziqniApiHttp: ZiqniApiHttp

  def ziqniTransformerEventBus: Option[ZiqniTransformerEventBus]

  def ziqniExecutionContext: ExecutionContextExecutor

  def ziqniSubAccounts: Seq[ZiqniAccount]

  def ziqniSubAccountApiAsync(spaceName: SpaceName): ZiqniApiAsync

  def ziqniConnectionParameterKeys(): Set[String]

  def ziqniConnectionParameter(connectionParameterKey:String): Option[AnyRef]

  def ziqniConnectionParameterAsString(connectionParameterKey:String): Option[String] = ziqniConnectionParameter(connectionParameterKey).map(_.toString)
  def ziqniConnectionParameterAsInt(connectionParameterKey:String): Option[Int] = ziqniConnectionParameter(connectionParameterKey).map(_.toString.toInt)
  def ziqniConnectionParameterAsDouble(connectionParameterKey:String): Option[Double] = ziqniConnectionParameter(connectionParameterKey).map(_.toString.toDouble)

  def ziqniSystemLogWriter(message: String, throwable: Throwable, logLevel: LogLevel): Unit
  def ziqniSystemLogWriter(message: String, description: String, logLevel: LogLevel): Unit = ziqniSystemLogWriter(message, new Exception(description), logLevel)

  /**
   * This is required to get around the, Dynamic Compilation Limitations
   * Scala's ToolBox is used for dynamically compiling and executing code. However, it sometimes struggles with Java interoperability, especially with complex or nested Java structures.
   * The ToolBox may not always correctly handle the classpath or dependencies required for these Java classes.
   **/
  lazy val aws: ZiqniAwsHelpers = new ZiqniAwsHelpers
}
