/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers.impl

import com.ziqni.transformers.domain.WebhookSettings
import com.ziqni.transformers.{ZiqniWebhookTransformer, ZiqniApi}
import org.joda.time.DateTime

class DefaultWebhookTransformer extends ZiqniWebhookTransformer {

	override def onNewProduct(settings: WebhookSettings, productId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"productId" -> productId,
			"productRefId" -> ziqniApi.productRefIdFromProductId(productId),
			"resourcePath" -> s"/products?id=$productId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "NewProduct",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onNewProduct")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onNewMember(settings: WebhookSettings, memberId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"memberId" -> memberId,
			"memberRefId" -> ziqniApi.memberRefIdFromMemberId(memberId),
			"resourcePath" -> s"/members?id=$memberId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "NewMember",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onNewMember")

		ziqniApi.httpPost(settings.url, json, headers)
	}


	override def onCompetitionCreated(settings: WebhookSettings, competitionId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/competitions?id=$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionCreated",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onCompetitionCreated")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionStarted(settings: WebhookSettings, competitionId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/competitions?id=$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionStarted",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onCompetitionStarted")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionFinished(settings: WebhookSettings, competitionId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/competitions?id=$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionFinished",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onCompetitionFinished")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionCancelled(settings: WebhookSettings, competitionId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/competitions?id=$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionCancelled",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onCompetitionCancelled")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionRewardIssued(settings: WebhookSettings, competitionId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionRewardIssued",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onCompetitionRewardIssued")

		ziqniApi.httpPost(settings.url, json, headers)
	}


	override def onContestCreated(settings: WebhookSettings, contestId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestCreated",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onContestCreated")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestStarted(settings: WebhookSettings, contestId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestStarted",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onContestStarted")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestFinished(settings: WebhookSettings, contestId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestFinished",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onContestFinished")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestFinalised(settings: WebhookSettings, contestId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestFinalised",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onContestFinalised")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestCancelled(settings: WebhookSettings, contestId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/contests?id=$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestCancelled",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onContestCancelled")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestRewardCreated(settings: WebhookSettings, rewardId: String, ziqniApi: ZiqniApi): Unit = {
		val body = Map[String, Any](
			"rewardId" -> rewardId,
			"timestamp" -> DateTime.now().getMillis,
			"resourcePath" -> s"/reward?id=$rewardId",
			"objectType" -> "ContestRewardCreated",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onContestRewardCreated")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestRewardIssued(settings: WebhookSettings, contestId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestRewardIssued",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onContestRewardIssued")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onContestRewardClaimed(settings: WebhookSettings, contestId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniApi: ZiqniApi): Unit = {
		val body = Map[String, Any](
			"contestId" -> contestId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestRewardClaimed",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onContestRewardClaimed")

		ziqniApi.httpPost(settings.url, json, headers)
	}


	override def onAchievementCreated(settings: WebhookSettings, achievementId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"resourcePath" -> s"/achievement?id=$achievementId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementCreated",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onAchievementCreated")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementTriggered(settings: WebhookSettings, achievementId: String, memberId: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniApi.memberRefIdFromMemberId(memberId),
			"resourcePath" -> s"/achievement?id=$achievementId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementTriggered",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onAchievementTriggered")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementRewardCreated(settings: WebhookSettings, rewardId: String, ziqniApi: ZiqniApi): Unit = {
		val body = Map[String, Any](
			"rewardId" -> rewardId,
			"timestamp" -> DateTime.now().getMillis,
			"resourcePath" -> s"/reward?id=$rewardId",
			"objectType" -> "AchievementRewardCreated",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onAchievementRewardCreated")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementRewardIssued(settings: WebhookSettings, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniApi: ZiqniApi): Unit = {

		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementRewardIssued",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onAchievementRewardIssued")

		ziqniApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementRewardClaimed(settings: WebhookSettings, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniApi: ZiqniApi): Unit = {
		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberRefId" -> ziqniApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/awards?id=$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementRewardClaimed",
			"spaceName" -> ziqniApi.spaceName
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader(ziqniApi.accountId, "onAchievementRewardClaimed")

		ziqniApi.httpPost(settings.url, json, headers)
	}

}
