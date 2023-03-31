package com.ziqni.transformers.webhooks;

/** *
 * Copyright (C) Ziqni Ltd - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Ziqni Ltd, 2023
 */

import org.joda.time.DateTime
import com.ziqni.transformers.ZiqniContext
import com.ziqni.transformers.domain.{BasicEntityChanged, BasicEntityStateChanged}
import com.ziqni.transformers.webhooks.ClassicWebhookSettings._

import scala.util.Try

/**
 * This is an implementation of the classic webhooks for backwards compatibility.
 */
trait ClassicWebhooks {

  ////////////////////////////////////////////////////////
  ///>>          WEBHOOK REPLACEMENT                 <<///
  ///>>Replace old webhooks with system notifications<<///
  ////////////////////////////////////////////////////////

  val TYPE_OF_CHANGE_CREATED = 1;
  val TYPE_OF_CHANGE_UPDATED = 2;
  val TYPE_OF_CHANGE_DELETED = 3;

  /**
   * If the transformer is subscribed to entity changes then this method is invoked
   *
   * @param change The change events
   */
  def onClassicEntityChanged(settings: ClassicWebhookSettings, change: BasicEntityChanged, ziqniContext: ZiqniContext): Unit = {
    implicit val z: ZiqniContext = ziqniContext
    implicit val c: BasicEntityChanged = change
    implicit val s: ClassicWebhookSettings = settings

    if (Product.equalsIgnoreCase(change.entityType))
      onClassicEntityChanged(onCreate = onNewProduct )

    else if (Member.equalsIgnoreCase(change.entityType))
      onClassicEntityChanged(onCreate = onNewMember )

    else if (Competition.equalsIgnoreCase(change.entityType))
      onClassicEntityChanged(onCreate = onCompetitionCreated )

    else if (Contest.equalsIgnoreCase(change.entityType))
      onClassicEntityChanged(onCreate = onContestCreated )



    else if (Achievement.equalsIgnoreCase(change.entityType))
      onClassicEntityChanged(onCreate = onAchievementCreated )

    else if (Reward.equalsIgnoreCase(change.entityType)){
      val entityType = change.metadata.getOrElse("parentType", "Unknown")

      if (entityType.equalsIgnoreCase(Contest)) {
        onContestRewardCreated()
      }
      else if (entityType.equalsIgnoreCase(Achievement)) {
        onAchievementRewardCreated()
      }
    }

    else if (Award.equalsIgnoreCase(change.entityType)){
      val entityType = change.metadata.getOrElse("parentType", "Unknown")

      if (entityType.equalsIgnoreCase(Competition)) {
        onCompetitionRewardIssued()
      }
      else if (entityType.equalsIgnoreCase(Contest)) {
        onContestRewardIssued()
      }
      else if (entityType.equalsIgnoreCase(Achievement)) {
        onAchievementRewardIssued()
      }
    }

    else {
      throw new Exception(s"Unsupported entity type, typeOffChange:${change.entityType} - ${change.toString}")
    }
  }

  private def onClassicEntityChanged(onCreate: () => Unit, onUpdate: () => Unit = () => {})(implicit change: BasicEntityChanged): Unit = {
    if (TYPE_OF_CHANGE_CREATED == change.typeOffChange)
      onCreate.apply()
    else if (TYPE_OF_CHANGE_UPDATED == change.typeOffChange)
      onUpdate.apply()
    else
      throw new Exception(s"Unsupported entity change, typeOffChange:${change.typeOffChange} - ${change.toString}")
  }

  def onClassicEntityStateChanged(settings: ClassicWebhookSettings, change: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit = {
    implicit val z: ZiqniContext = ziqniContext
    implicit val c: BasicEntityStateChanged = change
    implicit val s: ClassicWebhookSettings = settings

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
  }

  def onNewProduct()(implicit settings: ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onNewProductEnabled){
      val body = Map[String, Any](
        "productId" -> basicEntityChanged.entityId,
        "productRefId" -> basicEntityChanged.metadata.get("productRefId"),
        "resourcePath" -> s"/products?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "NewProduct",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onNewProduct")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onNewMember()(implicit settings:ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onNewMemberEnabled) {
      val body = Map[String, Any](
        "memberId" -> basicEntityChanged.entityId,
        "memberRefId" -> ziqniContext.ziqniApi.memberRefIdFromMemberId(basicEntityChanged.entityId),
        "resourcePath" -> s"/members?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "NewMember",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onNewMember")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }


  def onCompetitionCreated()(implicit settings:ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionCreatedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "CompetitionCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCreated")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onCompetitionStarted()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionStartedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "CompetitionStarted",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionStarted")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onCompetitionFinished()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionFinishedEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "CompetitionFinished",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionFinished")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onCompetitionCancelled()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionCancelledEnabled) {

      val body = Map[String, Any](
        "competitionId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/competitions?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "CompetitionCancelled",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCancelled")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onCompetitionRewardIssued()(implicit settings:ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onCompetitionRewardIssuedEnabled) {
      val claimed = Try(basicEntityChanged.metadata.getOrElse("claimed", "false").toBoolean).getOrElse(false)

      val body = Map[String, Any](
        "competitionId" -> basicEntityChanged.metadata.get("competitionId"),
        "memberId" -> basicEntityChanged.metadata.get("memberId"),
        "memberRefId" -> basicEntityChanged.metadata.get("memberRefId"),
        "awardId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/awards?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "CompetitionRewardIssued",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionRewardIssued")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }


  def onContestCreated()(implicit settings:ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onContestCreatedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestCreated")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onContestStarted()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onContestStartedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestStarted",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestStarted")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onContestFinished()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onContestFinishedEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestFinished",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinished")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onContestFinalised()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onContestFinalisedEnabled) {

    val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestFinalised",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinalised")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onContestCancelled()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onContestCancelledEnabled) {

      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/contests?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestCancelled",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestCancelled")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onContestRewardCreated()(implicit settings:ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardCreatedEnabled) {
      val body = Map[String, Any](
        "rewardId" -> basicEntityChanged.entityId,
        "timestamp" -> DateTime.now().getMillis,
        "resourcePath" -> s"/reward?id=${basicEntityChanged.entityId}",
        "objectType" -> "ContestRewardCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardCreated")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onContestRewardIssued()(implicit settings:ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
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

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardIssued")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onContestRewardClaimed()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onContestRewardClaimedEnabled) {
      val body = Map[String, Any](
        "contestId" -> basicEntityStateChanged.metadata.get("contestId"),
        "memberId" -> basicEntityStateChanged.metadata.get("memberId"),
        "memberRefId" -> basicEntityStateChanged.metadata.get("memberRefId"),
        "awardId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/awards?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "ContestRewardClaimed",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardClaimed")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }


  def onAchievementCreated()(implicit settings:ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementCreatedEnabled) {

      val body = Map[String, Any](
        "achievementId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/achievement?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "AchievementCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementCreated")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onAchievementRewardCreated()(implicit settings:ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardCreatedEnabled) {
      val body = Map[String, Any](
        "rewardId" -> basicEntityChanged.entityId,
        "timestamp" -> DateTime.now().getMillis,
        "resourcePath" -> s"/reward?id=${basicEntityChanged.entityId}",
        "objectType" -> "AchievementRewardCreated",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardCreated")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onAchievementRewardIssued()(implicit settings:ClassicWebhookSettings, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardIssuedEnabled) {

      val body = Map[String, Any](
        "achievementId" -> basicEntityChanged.metadata.get("achievementId"),
        "memberId" -> basicEntityChanged.metadata.get("memberId"),
        "memberRefId" -> basicEntityChanged.metadata.get("memberRefId"),
        "awardId" -> basicEntityChanged.entityId,
        "resourcePath" -> s"/awards?id=${basicEntityChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "AchievementRewardIssued",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardIssued")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }

  def onAchievementRewardClaimed()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, ziqniContext: ZiqniContext): Unit =
    if(settings.onAchievementRewardClaimedEnabled) {
      val body = Map[String, Any](
        "achievementId" -> basicEntityStateChanged.metadata.get("achievementId"),
        "memberId" -> basicEntityStateChanged.metadata.get("memberId"),
        "memberRefId" -> basicEntityStateChanged.metadata.get("memberRefId"),
        "awardId" -> basicEntityStateChanged.entityId,
        "resourcePath" -> s"/awards?id=${basicEntityStateChanged.entityId}",
        "timestamp" -> DateTime.now().getMillis,
        "objectType" -> "AchievementRewardClaimed",
        "spaceName" -> ziqniContext.spaceName
      )

      val json = ZiqniContext.toJsonFromMap(body)
      val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardClaimed")

      ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
    }


  //  @Deprecated("This created too much noise in the platform and negatively effected partner systems")
  //  def onAchievementTriggered()(implicit settings:ClassicWebhookSettings, basicEntityStateChanged: BasicEntityStateChanged, basicEntityChanged: BasicEntityChanged, ziqniContext: ZiqniContext): Unit = {
  //
  //    val body = Map[String, Any](
  //      "achievementId" -> basicEntityChanged.entityId,
  //      "memberId" -> basicEntityChanged.metadata.get("memberId"),
  //      "memberRefId" -> basicEntityChanged.metadata.get("memberRefId"),
  //      "resourcePath" -> s"/achievement?id=${basicEntityChanged.entityId}",
  //      "timestamp" -> DateTime.now().getMillis,
  //      "objectType" -> "AchievementTriggered",
  //      "spaceName" -> ziqniContext.spaceName
  //    )
  //
  //    val json = ZiqniContext.toJsonFromMap(body)
  //    val headers = settings.headers ++ ziqniContext.ziqniApiHttp.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementTriggered")
  //
  //    ziqniContext.ziqniApiHttp.httpPost(settings.url, json, headers)
  //  }
}


