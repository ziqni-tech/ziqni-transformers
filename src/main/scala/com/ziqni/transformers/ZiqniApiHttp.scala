/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers
import com.ziqni.transformers.domain._

import scala.concurrent.Future
trait ZiqniApiHttp {

	def HTTPDefaultHeader(accountId: String = "", onEvent: String = ""): Map[EventbusAddress, Seq[EventbusAddress]] = Map(
		"Content-Type" -> Seq("application/json"),
		"Content-Encoding" -> Seq("gzip"),
		"User_Agent" -> Seq("Ziqni/1.0"),
		"X-Account" -> Seq(accountId),
		"X-OnEvent" -> Seq(onEvent)
	)

	/**
	  * Send a http get request
	  * @param url The url to send the request too
	  * @param headers The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return HttpResponseEntity
	  */
	def httpGet(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, ziqniContext: ZiqniContext): HttpResponseEntity = httpGetWithLogMessage(url, headers, basicAuthCredentials, sendCompressed, None, ziqniContext)

	@Deprecated
	def httpGetWithLogMessage(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, logMessage: Option[String], ziqniContext: ZiqniContext): HttpResponseEntity
	def httpGetWithLogMessageAsync(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, logMessage: Option[String], ziqniContext: ZiqniContext): Future[HttpResponseEntity]

	/**
	  * Send a http put request
	  * @param url The url to send the request too
	  * @param body The request body
	  * @param headers The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return HttpResponseEntity
	  */
	def httpPut(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, ziqniContext: ZiqniContext): HttpResponseEntity = httpPutWithLogMessage(url, body, headers, basicAuthCredentials, sendCompressed, None, ziqniContext)

	@Deprecated
	def httpPutWithLogMessage(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, logMessage: Option[String], ziqniContext: ZiqniContext): HttpResponseEntity
	def httpPutWithLogMessageAsync(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, logMessage: Option[String], ziqniContext: ZiqniContext): Future[HttpResponseEntity]

	/**
	  * Send a http post request
	  * @param url The url to send the request too
	  * @param body The request body
	  * @param headers The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return HttpResponseEntity
	  */
	def httpPost(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, ziqniContext: ZiqniContext): HttpResponseEntity = httpPostWithLogMessage(url, body, headers, basicAuthCredentials, sendCompressed, None, ziqniContext)

	@Deprecated
	def httpPostWithLogMessage(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, logMessage: Option[String], ziqniContext: ZiqniContext): HttpResponseEntity
	def httpPostWithLogMessageAsync(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, logMessage: Option[String], ziqniContext: ZiqniContext): Future[HttpResponseEntity]

	/**
	  * Send a http delete request
	  * @param url The url to send the request too
	  * @param headers The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return HttpResponseEntity
	  */
	def httpDelete(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, ziqniContext: ZiqniContext): HttpResponseEntity = httpDeleteWithLogMessage(url, headers, basicAuthCredentials, sendCompressed, None, ziqniContext)

	@Deprecated
	def httpDeleteWithLogMessage(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, logMessage: Option[String], ziqniContext: ZiqniContext): HttpResponseEntity
	def httpDeleteWithLogMessageAsync(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[ZiqniAuthCredentials] = None, sendCompressed: Boolean = true, logMessage: Option[String], ziqniContext: ZiqniContext): Future[HttpResponseEntity]
}
