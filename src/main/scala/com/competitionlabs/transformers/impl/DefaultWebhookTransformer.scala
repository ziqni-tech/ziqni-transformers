/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2019
  */
package com.competitionlabs.transformers.impl

import com.competitionlabs.transformers.domain.WebhookSettings
import com.competitionlabs.transformers.{CLWebhookTransformer, CompetitionLabsApi}
import org.joda.time.DateTime

class DefaultWebhookTransformer extends CLWebhookTransformer {

	override def onNewProduct(settings: WebhookSettings, productId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"productId" -> productId,
			"productRefId" -> competitionLabsApi.productRefIdFromProductId(productId),
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/products/$productId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "NewProduct",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onNewProduct")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onNewMember(settings: WebhookSettings, memberId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"memberId" -> memberId,
			"memberRefId" -> competitionLabsApi.memberRefIdFromMemberId(memberId),
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/members/$memberId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "NewMember",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onNewMember")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}


	override def onCompetitionCreated(settings: WebhookSettings, competitionId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/competitions/$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionCreated",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onCompetitionCreated")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionStarted(settings: WebhookSettings, competitionId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/competitions/$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionStarted",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onCompetitionStarted")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionFinished(settings: WebhookSettings, competitionId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/competitions/$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionFinished",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onCompetitionFinished")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionCancelled(settings: WebhookSettings, competitionId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/competitions/$competitionId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionCancelled",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onCompetitionCancelled")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onCompetitionRewardIssued(settings: WebhookSettings, competitionId: String, memberId: String, awardId: String, rewardTypeKey: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"competitionId" -> competitionId,
			"memberId" -> memberId,
			"memberRefId" -> competitionLabsApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/awards/$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "CompetitionRewardIssued",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onCompetitionRewardIssued")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}


	override def onContestCreated(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/contests/$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestCreated",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onContestCreated")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onContestStarted(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/contests/$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestStarted",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onContestStarted")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onContestFinished(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/contests/$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestFinished",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onContestFinished")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onContestFinalised(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/contests/$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestFinalised",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onContestFinalised")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onContestCancelled(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/contests/$contestId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestCancelled",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onContestCancelled")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onContestRewardCreated(settings: WebhookSettings, rewardId: String, competitionLabsApi: CompetitionLabsApi): Unit = {
		val body = Map[String, Any](
			"rewardId" -> rewardId,
			"timestamp" -> DateTime.now().getMillis,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/reward/$rewardId",
			"objectType" -> "ContestRewardCreated",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onContestRewardCreated")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onContestRewardIssued(settings: WebhookSettings, contestId: String, memberId: String, awardId: String, rewardTypeKey: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"contestId" -> contestId,
			"memberId" -> memberId,
			"memberRefId" -> competitionLabsApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/awards/$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestRewardIssued",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onContestRewardIssued")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onContestRewardClaimed(settings: WebhookSettings, contestId: String, memberId: String, awardId: String, rewardTypeKey: String, competitionLabsApi: CompetitionLabsApi): Unit = {
		val body = Map[String, Any](
			"contestId" -> contestId,
			"memberId" -> memberId,
			"memberRefId" -> competitionLabsApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/awards/$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "ContestRewardClaimed",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onContestRewardClaimed")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}


	override def onAchievementCreated(settings: WebhookSettings, achievementId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/achievement/$achievementId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementCreated",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onAchievementCreated")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementTriggered(settings: WebhookSettings, achievementId: String, memberId: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberRefId" -> competitionLabsApi.memberRefIdFromMemberId(memberId),
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/achievement/$achievementId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementTriggered",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onAchievementTriggered")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementRewardCreated(settings: WebhookSettings, rewardId: String, competitionLabsApi: CompetitionLabsApi): Unit = {
		val body = Map[String, Any](
			"rewardId" -> rewardId,
			"timestamp" -> DateTime.now().getMillis,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/reward/$rewardId",
			"objectType" -> "AchievementRewardCreated",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onAchievementRewardCreated")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementRewardIssued(settings: WebhookSettings, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String, competitionLabsApi: CompetitionLabsApi): Unit = {

		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberRefId" -> competitionLabsApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/awards/$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementRewardIssued",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onAchievementRewardIssued")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

	override def onAchievementRewardClaimed(settings: WebhookSettings, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String, competitionLabsApi: CompetitionLabsApi): Unit = {
		val body = Map[String, Any](
			"achievementId" -> achievementId,
			"memberId" -> memberId,
			"memberRefId" -> competitionLabsApi.memberRefIdFromMemberId(memberId),
			"awardId" -> awardId,
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/awards/$awardId",
			"timestamp" -> DateTime.now().getMillis,
			"objectType" -> "AchievementRewardClaimed",
			"spaceName" -> competitionLabsApi.spaceName
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader(competitionLabsApi.accountId, "onAchievementRewardClaimed")

		competitionLabsApi.httpPost(settings.url, json, headers)
	}

}
