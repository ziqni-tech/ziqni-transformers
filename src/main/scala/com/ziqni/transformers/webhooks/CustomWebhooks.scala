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

  def doHttpPost(url: String, body: String, headers: Map[String, Seq[String]], basicAuthCredentials: Option[BasicAuthCredentials], sendCompressed: Boolean = true, logMessage: Option[String], ziqniContext: ZiqniContext): HttpResponseEntity =
    ziqniContext.ziqniApiHttp.httpPostWithLogMessage(url, body, headers, basicAuthCredentials, sendCompressed, logMessage, ziqniContext)

  def onNewProduct()(implicit settings: CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
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
          "timestamp" -> DateTime.now().getMillis,
          "objectType" -> "NewProduct",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = ZiqniContext.toJsonFromMap(body++additionalFields)
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onNewProduct")

        doHttpPost(settings.url, json, headers, settings.basicAuth, settings.sendCompressed, "onNewProduct", ziqniContext)
      }
    }

  def onNewMember()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onNewMemberEnabled) {
      implicit val e: ExecutionContextExecutor = ziqniContext.ziqniExecutionContext

      for {
        memberRefId <- ziqniContext.ziqniApiAsync.memberRefIdFromMemberId(basicEntityChanged.entityId)
      } yield {
        val body = Map[String, Any](
          "memberId" -> basicEntityChanged.entityId,
          "memberRefId" -> memberRefId,
          "resourcePath" -> s"/members?id=${basicEntityChanged.entityId}",
          "timestamp" -> DateTime.now().getMillis,
          "objectType" -> "NewMember",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = ZiqniContext.toJsonFromMap(body++additionalFields)
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onNewMember")

        doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onNewMember", ziqniContext)
      }
    }

  def onCompetitionCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionCreatedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "CompetitionCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCreated")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionCreated", ziqniContext)
    }

  def onCompetitionStarted()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionStartedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "CompetitionStarted",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionStarted")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionStarted", ziqniContext)
    }

  def onCompetitionFinished()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionFinishedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "CompetitionFinished",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionFinished")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionFinished", ziqniContext)
    }

  def onCompetitionCancelled()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionCancelledEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "CompetitionCancelled",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCancelled")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionCancelled", ziqniContext)
    }

  def onCompetitionRewardIssued()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit = {
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
          "timestamp" -> DateTime.now().getMillis,
          "objectType" -> "CompetitionRewardIssued",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = ZiqniContext.toJsonFromMap(body++additionalFields)
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionRewardIssued")

        doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onCompetitionRewardIssued", ziqniContext)
      }
    }
  }

  def onContestCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestCreatedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestCreated")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onContestCreated", ziqniContext)
    }

  def onContestStarted()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestStartedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestStarted",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestStarted")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onContestStarted", ziqniContext)
    }

  def onContestFinished()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestFinishedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestFinished",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinished")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onContestFinished", ziqniContext)
    }

  def onContestFinalised()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestFinalisedEnabled) {

    val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestFinalised",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinalised")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onContestFinalised", ziqniContext)
    }

  def onContestCancelled()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestCancelledEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestCancelled",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestCancelled")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onContestCancelled", ziqniContext)
    }

  def onContestRewardCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardCreatedEnabled) {
      val body = Map[String, Any](
        "rewardId" -> basicEntityChanged.entityId,
        "timestamp" -> DateTime.now().getMillis,
        "resourcePath" -> s"/reward?id=${basicEntityChanged.entityId}",
        "objectType" -> "ContestRewardCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardCreated")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardCreated", ziqniContext)
    }

  def onContestRewardIssued()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardIssuedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityChanged.metadata.get("contestId"),
        "memberId" -> basicEntityChanged.metadata.get("memberId"),
        "memberRefId" -> basicEntityChanged.metadata.get("memberRefId"),
        "awardId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/awards?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestRewardIssued",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardIssued")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardIssued", ziqniContext)
    }

  def onContestRewardClaimed()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
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
          "timestamp" -> DateTime.now().getMillis,
          "objectType" -> "ContestRewardClaimed",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = ZiqniContext.toJsonFromMap(body++additionalFields)
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardClaimed")

        doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onContestRewardClaimed", ziqniContext)
      }
    }

  def onAchievementCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementCreatedEnabled) {

      val body = Map[String, Any](
        "achievementId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/achievement?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "AchievementCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementCreated")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onAchievementCreated", ziqniContext)
    }

  def onAchievementRewardCreated()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardCreatedEnabled) {
      val body = Map[String, Any](
        "rewardId" -> basicEntityChanged.entityId,
        "timestamp" -> DateTime.now().getMillis,
        "resourcePath" -> s"/reward?id=${basicEntityChanged.entityId}",
        "objectType" -> "AchievementRewardCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body++additionalFields)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardCreated")

      doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardCreated", ziqniContext)
    }

  def onAchievementRewardIssued()(implicit settings:CustomWebhookSettings, basicEntityChanged: BasicEntityChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
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
          "timestamp" -> DateTime.now().getMillis,
          "objectType" -> "AchievementRewardIssued",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = ZiqniContext.toJsonFromMap(body++additionalFields)
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardIssued")

        doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardIssued", ziqniContext)
      }
    }

  def onAchievementRewardClaimed()(implicit settings:CustomWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, additionalFields: Map[String,Any], ziqniContext: ZiqniContext): Unit =
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
          "timestamp" -> DateTime.now().getMillis,
          "objectType" -> "AchievementRewardClaimed",
          "spaceName" -> ziqniContext.spaceName
        )

        val json = ZiqniContext.toJsonFromMap(body++additionalFields)
        val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardClaimed")

        doHttpPost(settings.url, json, headers, None, settings.sendCompressed, "onAchievementRewardClaimed", ziqniContext)
      }
    }

  private implicit def stringToOpt(s:String): Option[String] = Option(s)
}


