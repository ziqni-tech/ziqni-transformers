/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */
package com.competitionlabs.transformers.domain

object CLWebhookTriggers {

	type TriggerId = String
	type TriggerName = String
	type TriggerClass = String

	/**
	  * Use the simple class name to get the trigger id
	  */
	val TriggersByClassName = Map[TriggerClass, (TriggerId, TriggerName)](
		onNewProductTrigger.getClass.getSimpleName.replace("$","")          -> ("ET-1", "Product created"),

		onNewMemberTrigger.getClass.getSimpleName.replace("$","")           -> ("ET-2", "Member created"),

		onCompetitionCreatedTrigger.getClass.getSimpleName.replace("$","")  -> ("ET-3", "Competition created"),

		onCompetitionStartedTrigger.getClass.getSimpleName.replace("$","")  ->  ("ET-4", "Competition started"),

		onCompetitionFinishedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-5", "Competition finished"),

		onCompetitionCancelledTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-6", "Competition cancelled"),

		onCompetitionRewardIssuedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-7", "Competition reward issued"),

		onContestCreatedTrigger.getClass.getSimpleName.replace("$","")      ->  ("ET-8", "Contest created"),

		onContestStartedTrigger.getClass.getSimpleName.replace("$","")      ->  ("ET-9", "Contest started"),

		onContestFinishedTrigger.getClass.getSimpleName.replace("$","")     ->  ("ET-10", "Contest finished"),

		onContestFinalisedTrigger.getClass.getSimpleName.replace("$","")    ->  ("ET-10", "Contest finalised"),

		onContestCancelledTrigger.getClass.getSimpleName.replace("$","")    ->  ("ET-12", "Contest cancelled"),

		onContestRewardIssuedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-13", "Contest reward issued"),

		onAchievementCreatedTrigger.getClass.getSimpleName.replace("$","")  ->  ("ET-14", "Achievement created"),

		onAchievementTriggeredTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-15", "Achievement triggered"),

		onAchievementRewardIssuedTrigger.getClass.getSimpleName.replace("$","") ->  ("ET-16", "Achievement reward issued")
	)

	val TriggersById = TriggersByClassName.map(t => t._2._1 -> t._1)
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

	case class onContestRewardIssuedTrigger(accountId:String, contestId: String, memberId: String, awardId: String, rewardTypeKey: String) extends Serializable with WebhookTransformerTrigger

	case class onAchievementCreatedTrigger(accountId:String, achievementId: String) extends Serializable with WebhookTransformerTrigger

	case class onAchievementTriggeredTrigger(accountId:String, achievementId: String, memberId: String) extends Serializable with WebhookTransformerTrigger

	case class onAchievementRewardIssuedTrigger(accountId:String, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String) extends Serializable with WebhookTransformerTrigger
