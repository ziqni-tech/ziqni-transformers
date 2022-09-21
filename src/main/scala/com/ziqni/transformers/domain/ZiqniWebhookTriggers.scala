/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers.domain

object ZiqniWebhookTriggers {

	type TriggerId = String
	type TriggerName = String
	type TriggerClass = String

	/**
	  * Use the simple class name to get the trigger id
	  * TriggerIds ending in '01' or '51' correspond to create operations
	  * TriggerIds in '10s' correspond to actions
	  * TriggerIds in '60s' correspond to reward actions
	  */
	val TriggersByClassName: Map[TriggerClass, (TriggerId, TriggerName)] = Map[TriggerClass, (TriggerId, TriggerName)](
		onNewProductTrigger.getClass.getSimpleName.replace("$","")          -> ("ET-101", "Product created"),

		onNewMemberTrigger.getClass.getSimpleName.replace("$","")           -> ("ET-201", "Member created"),

		onCompetitionCreatedTrigger.getClass.getSimpleName.replace("$","")  -> ("ET-301", "Competition created"),

		onCompetitionStartedTrigger.getClass.getSimpleName.replace("$","")  ->  ("ET-311", "Competition started"),

		onCompetitionFinishedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-312", "Competition finished"),

		onCompetitionCancelledTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-313", "Competition cancelled"),

		onCompetitionRewardIssuedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-321", "Competition reward issued"),

		onContestCreatedTrigger.getClass.getSimpleName.replace("$","")      ->  ("ET-401", "Contest created"),

		onContestStartedTrigger.getClass.getSimpleName.replace("$","")      ->  ("ET-411", "Contest started"),

		onContestFinishedTrigger.getClass.getSimpleName.replace("$","")     ->  ("ET-412", "Contest finished"),

		onContestFinalisedTrigger.getClass.getSimpleName.replace("$","")    ->  ("ET-413", "Contest finalised"),

		onContestCancelledTrigger.getClass.getSimpleName.replace("$","")    ->  ("ET-414", "Contest cancelled"),

		onContestRewardCreatedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-451", "Contest reward created"),

		onContestRewardIssuedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-461", "Contest reward issued"),

		onContestRewardClaimedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-462", "Contest reward claimed"),

		onAchievementCreatedTrigger.getClass.getSimpleName.replace("$","")  ->  ("ET-501", "Achievement created"),

		onAchievementTriggeredTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-511", "Achievement triggered"),

		onAchievementRewardCreatedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-551", "Achievement reward created"),

		onAchievementRewardIssuedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-561", "Achievement reward issued"),

		onAchievementRewardClaimedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-562", "Achievement reward claimed")
	)

	val TriggersById: Map[TriggerId, TriggerClass] = TriggersByClassName.map(t => t._2._1 -> t._1)
}

trait WebhookTransformerTrigger{ val accountId: String }

case class onNewProductTrigger(accountId:String, productId: String) extends Serializable with WebhookTransformerTrigger

case class onNewMemberTrigger(accountId:String, memberId: String) extends Serializable with WebhookTransformerTrigger

case class onCompetitionCreatedTrigger(accountId:String, competitionId: String) extends Serializable with WebhookTransformerTrigger

case class onCompetitionStartedTrigger(accountId:String, competitionId: String) extends Serializable with WebhookTransformerTrigger

case class onCompetitionFinishedTrigger(accountId:String, competitionId: String) extends Serializable with WebhookTransformerTrigger

case class onCompetitionCancelledTrigger(accountId:String, competitionId: String) extends Serializable with WebhookTransformerTrigger

case class onCompetitionRewardIssuedTrigger(accountId:String, competitionId: String, memberId: String, awardId: String, rewardTypeKey: String) extends Serializable with WebhookTransformerTrigger

case class onContestCreatedTrigger(accountId:String, contestId: String) extends Serializable with WebhookTransformerTrigger

case class onContestStartedTrigger(accountId:String, contestId: String) extends Serializable with WebhookTransformerTrigger

case class onContestFinishedTrigger(accountId:String, contestId: String) extends Serializable with WebhookTransformerTrigger

case class onContestFinalisedTrigger(accountId:String, contestId: String) extends Serializable with WebhookTransformerTrigger

case class onContestCancelledTrigger(accountId:String, contestId: String) extends Serializable with WebhookTransformerTrigger

case class onContestRewardCreatedTrigger(accountId:String, rewardId: String) extends Serializable with WebhookTransformerTrigger

case class onContestRewardIssuedTrigger(accountId:String, contestId: String, memberId: String, awardId: String, rewardTypeKey: String) extends Serializable with WebhookTransformerTrigger

case class onContestRewardClaimedTrigger(accountId:String, contestId: String, memberId: String, awardId: String, rewardTypeKey: String) extends Serializable with WebhookTransformerTrigger

case class onAchievementCreatedTrigger(accountId:String, achievementId: String) extends Serializable with WebhookTransformerTrigger

case class onAchievementTriggeredTrigger(accountId:String, achievementId: String, memberId: String) extends Serializable with WebhookTransformerTrigger

case class onAchievementRewardCreatedTrigger(accountId:String, rewardId: String) extends Serializable with WebhookTransformerTrigger

case class onAchievementRewardIssuedTrigger(accountId:String, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String) extends Serializable with WebhookTransformerTrigger

case class onAchievementRewardClaimedTrigger(accountId:String, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String) extends Serializable with WebhookTransformerTrigger
