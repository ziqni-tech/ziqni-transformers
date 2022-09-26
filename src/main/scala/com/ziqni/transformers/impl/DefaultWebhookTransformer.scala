/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers.impl

import com.ziqni.transformers.domain.WebhookSettings
import com.ziqni.transformers.{ZiqniApi, ZiqniContext, ZiqniWebhookTransformer}
import com.ziqni.transformers.{ZiqniApi, ZiqniWebhookTransformer}
import org.joda.time.DateTime

class DefaultWebhookTransformer extends ZiqniWebhookTransformer {

	override def onNewProduct(settings: WebhookSettings, productId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"productId" -> productId,
			"productRefId" -> ziqniContext.ziqniApi.productRefIdFromProductId(productId),
			"resourcePath" -> s"/products?id=$productId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "NewProduct",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onNewProduct")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onNewMember(settings: WebhookSettings, memberId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"memberId" -> memberId,
			"memberRefId" -> ziqniContext.ziqniApi.memberRefIdFromMemberId(memberId),
			"resourcePath" -> s"/members?id=$memberId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "NewMember",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onNewMember")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}


	override def onCompetitionCreated(settings: WebhookSettings, competitionId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/competitions?id=$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionCreated",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCreated")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionStarted(settings: WebhookSettings, competitionId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/competitions?id=$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionStarted",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionStarted")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionFinished(settings: WebhookSettings, competitionId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/competitions?id=$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionFinished",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionFinished")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionCancelled(settings: WebhookSettings, competitionId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/competitions?id=$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionCancelled",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionCancelled")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionRewardIssued(settings: WebhookSettings, competitionId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniContext.ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionRewardIssued",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onCompetitionRewardIssued")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}


	override def onContestCreated(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestCreated",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onContestCreated")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestStarted(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestStarted",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onContestStarted")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestFinished(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestFinished",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinished")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestFinalised(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestFinalised",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onContestFinalised")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestCancelled(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestCancelled",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onContestCancelled")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestRewardCreated(settings: WebhookSettings, rewardId: String, ziqniContext: ZiqniContext): Unit = {
		val body = Map[String, Any](
			"rewardId" -> rewardId,
			"timestamp" -> DateTime.now().getMillis,
			"resourcePath" -> s"/reward?id=$rewardId",
			"objectType" -> "ContestRewardCreated",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardCreated")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestRewardIssued(settings: WebhookSettings, contestId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniContext.ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestRewardIssued",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardIssued")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestRewardClaimed(settings: WebhookSettings, contestId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {
		val body = Map[String, Any](
			"contestId" -> contestId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniContext.ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestRewardClaimed",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onContestRewardClaimed")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}


	override def onAchievementCreated(settings: WebhookSettings, achievementId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"resourcePath" -> s"/achievement?id=$achievementId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementCreated",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementCreated")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementTriggered(settings: WebhookSettings, achievementId: String, memberId: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniContext.ziqniApi.memberRefIdFromMemberId(memberId),
			"resourcePath" -> s"/achievement?id=$achievementId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementTriggered",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementTriggered")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementRewardCreated(settings: WebhookSettings, rewardId: String, ziqniContext: ZiqniContext): Unit = {
		val body = Map[String, Any](
			"rewardId" -> rewardId,
			"timestamp" -> DateTime.now().getMillis,
			"resourcePath" -> s"/reward?id=$rewardId",
			"objectType" -> "AchievementRewardCreated",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardCreated")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementRewardIssued(settings: WebhookSettings, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {

		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniContext.ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementRewardIssued",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardIssued")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementRewardClaimed(settings: WebhookSettings, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {
		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniContext.ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementRewardClaimed",
			"spaceName" -> ziqniContext.spaceName
		)

		val json =  ziqniContext.ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniContext.ziqniApi.HTTPDefaultHeader(ziqniContext.accountId, "onAchievementRewardClaimed")

		ziqniContext.ziqniApi.httpPost(settings.url, json, headers)
	}

}
