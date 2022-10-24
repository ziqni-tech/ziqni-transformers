package com.ziqni.transformers

import scala.concurrent.ExecutionContextExecutor
import org.json4s.jackson.JsonMethods.parse
import org.json4s.jackson.Serialization
import org.json4s.{DefaultFormats, JValue}

import java.nio.charset.Charset

object ZiqniContext {

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
  def fromJsonString(body: String): JValue = parse(body)

  /**
    * Converts a map to a json string
    *
    * @param obj The object to serialise
    * @return json string or throws exception
    */
  def toJsonFromMap(obj: Map[String, Any]): String = Serialization.write(obj)(DefaultFormats)
}

case class ZiqniContext(accountId: String,
                        spaceName: String,
                        ziqniTransformerInfo: ZiqniTransformerInfo,
                        ziqniApi: ZiqniApi,
                        ziqniApiAsync: ZiqniApiAsync,
                        ziqniApiHttp: ZiqniApiHttp,
                        ziqniTransformerEventBus:Option[ZiqniTransformerEventBus],
                        ziqniExecutionContext: ExecutionContextExecutor)
