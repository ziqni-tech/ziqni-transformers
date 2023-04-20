package com.ziqni.transformers.webhooks;

/** *
 * Copyright (C) Ziqni Ltd - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ziqni Ltd, 2023
 */

import org.joda.time.DateTime
import com.ziqni.transformers.ZiqniContext
import com.ziqni.transformers.domain.{BasicAuthCredentials, BasicEntityChanged, BasicEntityStateChanged, HttpResponseEntity}
import com.ziqni.transformers.webhooks.CustomWebhookSettings._

import java.security.Timestamp
import scala.concurrent.ExecutionContextExecutor
import scala.language.implicitConversions

/**
 * This is an implementation of the classic webhooks for backwards compatibility and ease of customisation.
 */
trait CustomWebhooks {

  ////////////////////////////////////////////////////////////
  /// >>             CUSTOM WEBHOOK REPLACEMENT         << ///
  /// >> Replace old webhooks with system notifications << ///
  ////////////////////////////////////////////////////////////


  /**
   * If the transformer is subscribed to entity changes then this method is invoked
   *
   * @param change The change events
   */
  def onCustomEntityChanged(settings: CustomWebhookSettings, change: BasicEntityChanged, ziqniContext: ZiqniContext): Unit = {
    implicit val z: ZiqniContext = ziqniContext
    implicit val c: BasicEntityChanged = change
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

      if (claimed && entityType.equalsIgnoreCase(Competition)) {
        onCompetitionRewardIssued()
      }
      else if (claimed && entityType.equalsIgnoreCase(Contest)) {
        onContestRewardIssued()
      }
      else if (claimed && entityType.equalsIgnoreCase(Achievement)) {
        onAchievementRewardIssued()
      }
    }
  }

  private def onCustomEntityChanged(onCreate: () => Unit, onUpdate: () => Unit = () => {})(implicit change: BasicEntityChanged): Unit = {
    if (TYPE_OF_CHANGE_CREATED == change.typeOffChange)
      onCreate.apply()
    else if (TYPE_OF_CHANGE_UPDATED == change.typeOffChange)
      onUpdate.apply()
  }

  def onCustomEntityStateChanged(settings: CustomWebhookSettings, change: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit = {
    implicit val z: ZiqniContext = ziqniContext
    implicit val c: BasicEntityStateChanged = change
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
      val claimed = change.metadata.getOrElse(Claimed, "false").toBoolean

      if(claimed && change.currentState > 0 && entityType.equalsIgnoreCase(Contest))
        onContestRewardClaimed()
      else if(claimed && change.currentState > 0 && entityType.equalsIgnoreCase(Achievement))
        onAchievementRewardClaimed()
    }
  }

  def httpPreFlightHeaders(headers: Map[String, Seq[String]], body: String, timestamp: DateTime)(implicit settings: CustomWebhookSettings, ziqniContext: ZiqniContext): Map[String, Seq[String]] = headers

  def httpPreFlightBody(body: Map[String, Any], timestamp: DateTime)(implicit settings: CustomWebhookSettings, ziqniContext: ZiqniContext): String = ZiqniContext.toJsonFromMap(body)

  def doHttpPost(url: String, body: String, headers: Map[String, Seq[String]], basicAuthCredentials: Option[BasicAuthCredentials], sendCompressed: Boolean = true, logMessage: Option[String])(implicit settings: CustomWebhookSettings, ziqniContext: ZiqniContext): HttpResponseEntity =
    ziqniContext.ziqniApiHttp.httpPostWithLogMessage(url, body, httpPreFlightHeaders(headers, body, DateTime.now()), basicAuthCredentials, sendCompressed, logMessage, ziqniContext)

  def doHttpPostAsMap(url: String, body: Map[String, Any], headers: Map[String, Seq[String]], basicAuthCredentials: Option[BasicAuthCredentials], sendCompressed: Boolean = true, logMessage: Option[String])(implicit settings: CustomWebhookSettings, ziqniContext: ZiqniContext): HttpResponseEntity = {
    val ts = DateTime.now()
    val bodyString = httpPreFlightBody(body, ts)
    ziqniContext.ziqniApiHttp.httpPostWithLogMessage(url, bodyString, httpPreFlightHeaders(headers, bodyString, ts), basicAuthCredentials, sendCompressed, logMessage, ziqniContext)
  }

  def onNewProduct()(implicit settings: CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onNewProductEnabled){
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext

      for {
        productRefId <- ziqniContext.ziqniApiAsync.productRefIdFromProductId(basicEntityChanged.entityId)
      } yield
      {
        val body = Map[String, Any](
          "productId" -> basicEntityChanged.entityId,
          "productRefId" -> productRefId,
          "resourcePath" -> s"/products?id=${basicEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "NewProduct",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onNewProduct")

        doHttpPostAsMap(settings.url, json, headers, settings.basicAuth, settings.sendCompressed, "onNewProduct")
      }
    }

  def onNewMember()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onNewMemberEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext

      for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(basicEntityChanged.entityId)
      } yield {
        val body = Map[String, Any](
          "memberId" -> basicEntityChanged.entityId,
          "memberRefId" -> memberRefId,
          "resourcePath" -> s"/members?id=${basicEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "NewMember",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onNewMember")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onNewMember")
      }
    }

  def onCompetitionCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionCreatedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "CompetitionCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionCreated")
    }

  def onCompetitionStarted()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionStartedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "CompetitionStarted",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionStarted")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionStarted")
    }

  def onCompetitionFinished()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionFinishedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "CompetitionFinished",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionFinished")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionFinished")
    }

  def onCompetitionCancelled()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionCancelledEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "CompetitionCancelled",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCancelled")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionCancelled")
    }

  def onCompetitionRewardIssued()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit = {
    if(settings.onCompetitionRewardIssuedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = basicEntityChanged.metadata.getOrElse("memberId","")

      for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "competitionId" -> basicEntityChanged.metadata.get("competitionId"),
          "memberId" -> memberId,
          "memberRefId" -> memberRefId,
          "awardId" -> basicEntityChanged.entityId,
          "resourcePath" -> s"/awards?id=${basicEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "CompetitionRewardIssued",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionRewardIssued")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionRewardIssued")
      }
    }
  }

  def onContestCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestCreatedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestCreated")
    }

  def onContestStarted()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestStartedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestStarted",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestStarted")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestStarted")
    }

  def onContestFinished()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestFinishedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestFinished",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinished")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestFinished")
    }

  def onContestFinalised()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestFinalisedEnabled) {

    val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestFinalised",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinalised")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestFinalised")
    }

  def onContestCancelled()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestCancelledEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestCancelled",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestCancelled")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestCancelled")
    }

  def onContestRewardCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardCreatedEnabled) {
      val body = Map[String, Any](
        "rewardId" -> basicEntityChanged.entityId,
        "timestamp" -> timestamp.getMillis,
        "resourcePath" -> s"/reward?id=${basicEntityChanged.entityId}",
        "objectType" -> "ContestRewardCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardCreated")
    }

  def onContestRewardIssued()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardIssuedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityChanged.metadata.get("contestId"),
        "memberId" -> basicEntityChanged.metadata.get("memberId"),
        "memberRefId" -> basicEntityChanged.metadata.get("memberRefId"),
        "awardId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/awards?id=${basicEntityChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "ContestRewardIssued",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardIssued")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardIssued")
    }

  def onContestRewardClaimed()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardClaimedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = basicEntityStateChanged.metadata.getOrElse("memberId", "")

      for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "contestId" -> basicEntityStateChanged.metadata.get("contestId"),
          "memberId" -> memberId,
          "memberRefId" -> memberRefId,
          "awardId" -> basicEntityStateChanged.entityId,
          "resourcePath" -> s"/awards?id=${basicEntityStateChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "ContestRewardClaimed",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardClaimed")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardClaimed")
      }
    }

  def onAchievementCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementCreatedEnabled) {

      val body = Map[String, Any](
        "achievementId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/achievement?id=${basicEntityChanged.entityId}",
        "timestamp" -> timestamp.getMillis,
        "objectType" -> "AchievementCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onAchievementCreated")
    }

  def onAchievementRewardCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardCreatedEnabled) {
      val body = Map[String, Any](
        "rewardId" -> basicEntityChanged.entityId,
        "timestamp" -> timestamp.getMillis,
        "resourcePath" -> s"/reward?id=${basicEntityChanged.entityId}",
        "objectType" -> "AchievementRewardCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = body++additionalFields
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardCreated")

      doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardCreated")
    }

  def onAchievementRewardIssued()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardIssuedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = basicEntityChanged.metadata.getOrElse("memberId", "")

      for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "achievementId" -> basicEntityChanged.metadata.get("achievementId"),
          "memberId" -> basicEntityChanged.metadata.get("memberId"),
          "memberRefId" -> memberRefId,
          "awardId" -> basicEntityChanged.entityId,
          "resourcePath" -> s"/awards?id=${basicEntityChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "AchievementRewardIssued",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardIssued")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardIssued")
      }
    }

  def onAchievementRewardClaimed()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, timestamp: DateTime, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardClaimedEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext
      val memberId = basicEntityStateChanged.metadata.getOrElse("memberId", "")

      for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(memberId)
      } yield {
        val body = Map[String, Any](
          "achievementId" -> basicEntityStateChanged.metadata.get("achievementId"),
          "memberId" -> memberId,
          "memberRefId" -> memberRefId,
          "awardId" -> basicEntityStateChanged.entityId,
          "resourcePath" -> s"/awards?id=${basicEntityStateChanged.entityId}",
          "timestamp" -> timestamp.getMillis,
          "objectType" -> "AchievementRewardClaimed",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = body++additionalFields
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardClaimed")

        doHttpPostAsMap(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardClaimed")
      }
    }

  private implicit def stringToOpt(s:String): Option[String] = Option(s)
}

