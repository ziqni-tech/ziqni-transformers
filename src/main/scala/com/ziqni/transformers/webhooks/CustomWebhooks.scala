package com.ziqni.transformers.webhooks;

/** *
 * Copyright (C) Ziqni Ltd - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ziqni Ltd, 2023
 */

import org.joda.time.DateTime
import com.ziqni.transformers.{LogLevel, ZiqniContext}
import com.ziqni.transformers.webhooks.CustomWebhookSettings._
import com.ziqni.transformers.domain._

import scala.concurrent.ExecutionContextExecutor
import scala.language.implicitConversions
import scala.util.control.NonFatal

/**
 * This is an implementation of the custom webhooks for backwards compatibility and ease of customisation.
 */
class CustomWebhooks {

  ////////////////////////////////////////////////////////////
  /// >>             CUSTOM WEBHOOK REPLACEMENT         << ///
  /// >> Replace old webhooks with system notifications << ///
  ////////////////////////////////////////////////////////////


  /**
   * If the transformer is subscribed to entity changes then this method is invoked
   *
   * @param change The change events
   */
  def onCustomEntityChanged(settings: CustomWebhookSettings, change: ZiqniEntityChanged, ziqniContext: ZiqniContext): Unit = {
    implicit val z: ZiqniContext = ziqniContext
    implicit val c: ZiqniEntityChanged = change
    implicit val s: CustomWebhookSettings = settings
    implicit val a: Map[String, Any] = Map.empty
    implicit val t: DateTime = DateTime.now()

    if (Product.equalsIgnoreCase(change.entityType))
      onCustomEntityChanged(onCreate = onNewProduct )

    else if (Member.equalsIgnoreCase(change.entityType))
      onCustomEntityChanged(onCreate = onNewMember )

    else if (Competition.equalsIgnoreCase(change.entityType))
      onCustomEntityChanged(onCreate = onCompetitionCreated )

    else if (Contest.equalsIgnoreCase(change.entityType))
      onCustomEntityChanged(onCreate = onContestCreated )



    else if (Achievement.equalsIgnoreCase(change.entityType))
      onCustomEntityChanged(onCreate = onAchievementCreated )

    else if (Reward.equalsIgnoreCase(change.entityType)){
      val entityType = change.metadata.getOrElse(ParentType, Unknown)

      if (entityType.equalsIgnoreCase(Contest)) {
        onContestRewardCreated()
      }
      else if (entityType.equalsIgnoreCase(Achievement)) {
        onAchievementRewardCreated()
      }
    }

    else if (Award.equalsIgnoreCase(change.entityType)){
      val entityType = change.metadata.getOrElse(ParentType, Unknown)
      val claimed = change.metadata.getOrElse(Claimed, "false").toBoolean


      if (entityType.equalsIgnoreCase(Competition)) {
        onCompetitionRewardIssued()
      }
      else if (entityType.equalsIgnoreCase(Contest)) {
        if(claimed)
          onContestRewardClaimedZiqniEntityChanged()
        else
          onContestRewardIssued()

      }
      else if (entityType.equalsIgnoreCase(Achievement)) {
        if (claimed)
          onAchievementRewardClaimedZiqniEntityChanged()
        else
          onAchievementRewardIssued()
      }
    }
  }

  private def onCustomEntityChanged(onCreate: () => Unit, onUpdate: () => Unit = () => {})(implicit change: ZiqniEntityChanged): Unit = {
    if (TYPE_OF_CHANGE_CREATED == change.typeOffChange)
      onCreate.apply()
    else if (TYPE_OF_CHANGE_UPDATED == change.typeOffChange)
      onUpdate.apply()
  }

  def onCustomEntityStateChanged(settings: CustomWebhookSettings, change: ZiqniEntityStateChanged, ziqniContext: ZiqniContext): Unit = {
    implicit val z: ZiqniContext = ziqniContext
    implicit val c: ZiqniEntityStateChanged = change
    implicit val s: CustomWebhookSettings = settings
    implicit val a: Map[String,Any] = Map.empty
    implicit val t: DateTime = DateTime.now()

    if (Competition.equalsIgnoreCase(change.entityType)) {
      if(change.currentState==25)
        onCompetitionStarted()
      else if(change.currentState==35)
        onCompetitionFinished()
      else if(change.currentState==115)
        onCompetitionCancelled()
    }

    else if (Contest.equalsIgnoreCase(change.entityType)) {
      if(change.currentState==25)
        onContestStarted()
      else if(change.currentState==35)
        onContestFinished()
      else if(change.currentState== 45)
        onContestFinalised()
      else if(change.currentState==115)
        onContestCancelled()
    }

    else if (Award.equalsIgnoreCase(change.entityType)) {
      val entityType = change.metadata.getOrElse(ParentType, Unknown)
      val claimed = change.metadata.getOrElse(Claimed,false).toString.toBoolean


      if(claimed && change.currentState > 0 && entityType.equalsIgnoreCase(Contest))
        onContestRewardClaimed()
      else if(claimed && change.currentState > 0 && entityType.equalsIgnoreCase(Achievement))
        onAchievementRewardClaimed()
    }
  }

  def httpPreFlightHeaders(headers: Map[String, Seq[String]], body: String, timestamp: DateTime)(implicit settings: CustomWebhookSettings, ziqniContext: ZiqniContext): Map[String, Seq[String]] = headers

  def httpPreFlightBody(body: Map[String, Any], timestamp: DateTime)(implicit settings: CustomWebhookSettings, ziqniContext: ZiqniContext): String = ZiqniContext.toJsonFromMap(body)

  def doHttpPost(url: String, body: String, headers: Map[String, Seq[String]], basicAuthCredentials: Option[ZiqniAuthCredentials], sendCompressed: Boolean = true, logMessage: Option[String])(implicit settings: CustomWebhookSettings, ziqniContext: ZiqniContext): HttpResponseEntity =
    ziqniContext.ziqniApiHttp.httpPostWithLogMessage(url, body, httpPreFlightHeaders(headers, body, DateTime.now()), basicAuthCredentials, sendCompressed, logMessage, ziqniContext)

  def doHttpPostAsMap(url: String, body: Map[String, Any], headers: Map[String, Seq[String]], basicAuthCredentials: Option[ZiqniAuthCredentials], sendCompressed: Boolean = true, logMessage: Option[String])(implicit settings: CustomWebhookSettings, ziqniContext: ZiqniContext): HttpResponseEntity = {
    val ts = DateTime.now()
    val bodyString = httpPreFlightBody(body, ts)
    ziqniContext.ziqniApiHttp.httpPostWithLogMessage(url, bodyString, httpPreFlightHeaders(headers, bodyString, ts), basicAuthCredentials, sendCompressed, logMessage, ziqniContext)
  }

  def onNewProduct()(implicit settings: CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onNewProductEnabled){
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext

      val result = for {
        productRefId <- ziqniContext.ziqniApiAsync.productRefIdFromProductId(ziqniEntityChanged.entityId)
      } yield
      {
        val body = Map[String, Any](
          "productId" -> ziqniEntityChanged.entityId,
          "productRefId" -> productRefId,
          "resourcePath" -> s"/products?id=${ziqniEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "NewProduct",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onNewProduct")

        doHttpPostAsMap(settings.url, json, headers, settings.basicAuth, settings.sendCompressed, "onNewProduct")
      }

      result.recover({
        case NonFatal(e) => ziqniContext.ziqniSystemLogWriter("onNewProduct", e, LogLevel.ERROR)
      })
    }

  def onNewMember()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onNewMemberEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext

      val result = for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(ziqniEntityChanged.entityId)
      } yield {
        val body = Map[String, Any](
          "memberId" -> ziqniEntityChanged.entityId,
          "memberRefId" -> memberRefId,
          "resourcePath" -> s"/members?id=${ziqniEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "NewMember",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onNewMember")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onNewMember")
      }

      result.recover({
        case NonFatal(e) => ziqniContext.ziqniSystemLogWriter("onNewMember", e, LogLevel.ERROR)
      })
    }

  def onCompetitionCreated()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionCreatedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> ziqniEntityChanged.entityId,
        "resourcePath" -> s"/competitions?id=${ziqniEntityChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "CompetitionCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionCreated")
    }

  def onCompetitionStarted()(implicit settings:CustomWebhookSettings, ziqniEntityStateChanged: ZiqniEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionStartedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> ziqniEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${ziqniEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "CompetitionStarted",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionStarted")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionStarted")
    }

  def onCompetitionFinished()(implicit settings:CustomWebhookSettings, ziqniEntityStateChanged: ZiqniEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionFinishedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> ziqniEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${ziqniEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "CompetitionFinished",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionFinished")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionFinished")
    }

  def onCompetitionCancelled()(implicit settings:CustomWebhookSettings, ziqniEntityStateChanged: ZiqniEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionCancelledEnabled) {

      val body = Map[String, Any](
        "competitionId" -> ziqniEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${ziqniEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "CompetitionCancelled",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCancelled")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionCancelled")
    }

  def onCompetitionRewardIssued()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit = {
    if(settings.onCompetitionRewardIssuedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = ziqniEntityChanged.metadata.getOrElse("memberId","")

      val result = for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "competitionId" -> ziqniEntityChanged.metadata.get("parentId"),
          "memberId" -> memberId,
          "memberRefId" -> memberRefId,
          "awardId" -> ziqniEntityChanged.entityId,
          "resourcePath" -> s"/awards?id=${ziqniEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "CompetitionRewardIssued",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionRewardIssued")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionRewardIssued")
      }

      result.recover({
        case NonFatal(e) => ziqniContext.ziqniSystemLogWriter("onCompetitionRewardIssued", e, LogLevel.ERROR)
      })
    }
  }

  def onContestCreated()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestCreatedEnabled) {

      val body = Map[String, Any](
        "contestId" -> ziqniEntityChanged.entityId,
        "resourcePath" -> s"/contests?id=${ziqniEntityChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestCreated")
    }

  def onContestStarted()(implicit settings:CustomWebhookSettings, ziqniEntityStateChanged: ZiqniEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestStartedEnabled) {

      val body = Map[String, Any](
        "contestId" -> ziqniEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${ziqniEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestStarted",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestStarted")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestStarted")
    }

  def onContestFinished()(implicit settings:CustomWebhookSettings, ziqniEntityStateChanged: ZiqniEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestFinishedEnabled) {

      val body = Map[String, Any](
        "contestId" -> ziqniEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${ziqniEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestFinished",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinished")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestFinished")
    }

  def onContestFinalised()(implicit settings:CustomWebhookSettings, ziqniEntityStateChanged: ZiqniEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestFinalisedEnabled) {

    val body = Map[String, Any](
        "contestId" -> ziqniEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${ziqniEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestFinalised",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinalised")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestFinalised")
    }

  def onContestCancelled()(implicit settings:CustomWebhookSettings, ziqniEntityStateChanged: ZiqniEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestCancelledEnabled) {

      val body = Map[String, Any](
        "contestId" -> ziqniEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${ziqniEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestCancelled",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestCancelled")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestCancelled")
    }

  def onContestRewardCreated()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardCreatedEnabled) {
      val body = Map[String, Any](
        "rewardId" -> ziqniEntityChanged.entityId,
        "timestamp" -> timestamp.getMillis,
        "resourcePath" -> s"/reward?id=${ziqniEntityChanged.entityId}",
        "objectType" -> "ContestRewardCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardCreated")
    }

  def onContestRewardIssued()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardIssuedEnabled) {

      val body = Map[String, Any](
        "contestId" -> ziqniEntityChanged.metadata.get("parentId"),
        "memberId" -> ziqniEntityChanged.metadata.get("memberId"),
        "memberRefId" -> ziqniEntityChanged.metadata.get("memberRefId"),
        "awardId" -> ziqniEntityChanged.entityId,
        "resourcePath" -> s"/awards?id=${ziqniEntityChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestRewardIssued",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardIssued")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardIssued")
    }

  def onContestRewardClaimed()(implicit settings:CustomWebhookSettings, ziqniEntityStateChanged: ZiqniEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardClaimedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = ziqniEntityStateChanged.metadata.getOrElse("memberId", ziqniEntityStateChanged.memberId)

      val result = for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "contestId" -> ziqniEntityStateChanged.metadata.get("parentId"),
          "memberId" -> memberId,
          "memberRefId" -> memberRefId,
          "awardId" -> ziqniEntityStateChanged.entityId,
          "resourcePath" -> s"/awards?id=${ziqniEntityStateChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "ContestRewardClaimed",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardClaimed")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardClaimed")
      }

      result.recover({
        case NonFatal(e) => ziqniContext.ziqniSystemLogWriter("onContestRewardClaimed", e, LogLevel.ERROR)
      })
    }

  def onContestRewardClaimedZiqniEntityChanged()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardClaimedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = ziqniEntityChanged.metadata.getOrElse("memberId", "")

      val result = for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "contestId" -> ziqniEntityChanged.metadata.get("parentId"),
          "memberId" -> memberId,
          "memberRefId" -> memberRefId,
          "awardId" -> ziqniEntityChanged.entityId,
          "resourcePath" -> s"/awards?id=${ziqniEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "ContestRewardClaimed",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardClaimed")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardClaimed")
      }

      result.recover({
        case NonFatal(e) => ziqniContext.ziqniSystemLogWriter("onContestRewardClaimed", e, LogLevel.ERROR)
      })
    }


  def onAchievementCreated()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementCreatedEnabled) {

      val body = Map[String, Any](
        "achievementId" -> ziqniEntityChanged.entityId,
        "resourcePath" -> s"/achievement?id=${ziqniEntityChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "AchievementCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onAchievementCreated")
    }

  def onAchievementRewardCreated()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardCreatedEnabled) {
      val body = Map[String, Any](
        "rewardId" -> ziqniEntityChanged.entityId,
        "timestamp" -> timestamp.getMillis,
        "resourcePath" -> s"/reward?id=${ziqniEntityChanged.entityId}",
        "objectType" -> "AchievementRewardCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardCreated")
    }

  def onAchievementRewardIssued()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardIssuedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = ziqniEntityChanged.metadata.getOrElse("memberId", "")

      val result = for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "achievementId" -> ziqniEntityChanged.metadata.get("parentId"),
          "memberId" -> memberId,
          "memberRefId" -> memberRefId,
          "awardId" -> ziqniEntityChanged.entityId,
          "resourcePath" -> s"/awards?id=${ziqniEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "AchievementRewardIssued",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardIssued")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardIssued")
      }

      result.recover({
        case NonFatal(e) => ziqniContext.ziqniSystemLogWriter("onAchievementRewardIssued", e, LogLevel.ERROR)
      })
    }

  def onAchievementRewardClaimed()(implicit settings:CustomWebhookSettings, ziqniEntityStateChanged: ZiqniEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardClaimedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = ziqniEntityStateChanged.metadata.getOrElse("memberId", ziqniEntityStateChanged.memberId)

      val result = for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "achievementId" -> ziqniEntityStateChanged.metadata.get("parentId"),
          "memberId" -> memberId,
          "memberRefId" -> memberRefId,
          "awardId" -> ziqniEntityStateChanged.entityId,
          "resourcePath" -> s"/awards?id=${ziqniEntityStateChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "AchievementRewardClaimed",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardClaimed")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardClaimed")
      }

      result.recover({
        case NonFatal(e) => ziqniContext.ziqniSystemLogWriter("onAchievementRewardClaimed", e, LogLevel.ERROR)
      })
    }

  def onAchievementRewardClaimedZiqniEntityChanged()(implicit settings:CustomWebhookSettings, ziqniEntityChanged: ZiqniEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardClaimedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = ziqniEntityChanged.metadata.getOrElse("memberId", "")

      val result = for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "achievementId" -> ziqniEntityChanged.metadata.get("parentId"),
          "memberId" -> memberId,
          "memberRefId" -> memberRefId,
          "awardId" -> ziqniEntityChanged.entityId,
          "resourcePath" -> s"/awards?id=${ziqniEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "AchievementRewardClaimed",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardClaimed")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardClaimed")
      }

      result.recover({
        case NonFatal(e) => ziqniContext.ziqniSystemLogWriter("onAchievementRewardClaimed", e, LogLevel.ERROR)
      })
    }

  private implicit def stringToOpt(s:String): Option[String] = Option(s)
}


