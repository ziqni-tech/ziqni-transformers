/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2019
  */
package com.competitionlabs.transformers

import com.competitionlabs.transformers.domain.{BasicAuthCredentials, HttpResponseEntity}

trait CompetitionLabsApiHttp {
	
	def HTTPDefaultHeader(accountId: String = "", onEvent: String = "") = Map(
		"Content-Type" -> Seq("application/json"),
		"Content-Encoding" -> Seq("gzip"),
		"User_Agent" -> Seq("CompetitionLabs/1.0"),
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
	def httpGet(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[BasicAuthCredentials] = None, sendCompressed: Boolean = true): HttpResponseEntity

	/**
	  * Send a http put request
	  * @param url The url to send the request too
	  * @param body The request body
	  * @param headers The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return HttpResponseEntity
	  */
	def httpPut(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[BasicAuthCredentials] = None, sendCompressed: Boolean = true): HttpResponseEntity

	/**
	  * Send a http post request
	  * @param url The url to send the request too
	  * @param body The request body
	  * @param headers The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return HttpResponseEntity
	  */
	def httpPost(url: String, body: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[BasicAuthCredentials] = None, sendCompressed: Boolean = true): HttpResponseEntity

	/**
	  * Send a http delete request
	  * @param url The url to send the request too
	  * @param headers The request headers
	  * @param basicAuthCredentials Basic authentication
	  * @return HttpResponseEntity
	  */
	def httpDelete(url: String, headers: Map[String, Seq[String]] = HTTPDefaultHeader(), basicAuthCredentials: Option[BasicAuthCredentials] = None, sendCompressed: Boolean = true): HttpResponseEntity
}
