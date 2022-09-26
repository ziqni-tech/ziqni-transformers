/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers.factory

import com.ziqni.transformers.{ZiqniApi, ZiqniContext, ZiqniWebhookTransformer}
import com.ziqni.transformers.domain.{WebhookSettings, WebhookTransformerTrigger, onAchievementCreatedTrigger, onAchievementRewardClaimedTrigger, onAchievementRewardCreatedTrigger, onAchievementRewardIssuedTrigger, onAchievementTriggeredTrigger, onCompetitionCancelledTrigger, onCompetitionCreatedTrigger, onCompetitionFinishedTrigger, onCompetitionRewardIssuedTrigger, onCompetitionStartedTrigger, onContestCancelledTrigger, onContestCreatedTrigger, onContestFinalisedTrigger, onContestFinishedTrigger, onContestRewardClaimedTrigger, onContestRewardCreatedTrigger, onContestRewardIssuedTrigger, onContestStartedTrigger, onNewMemberTrigger, onNewProductTrigger}
import com.ziqni.transformers.impl.DefaultWebhookTransformer
import com.ziqni.transformers.domain._

object ZiqniWebhookTransformerFactory {

	val defaultWebhookTransformer = new DefaultWebhookTransformer

	def apply[T <: WebhookTransformerTrigger](trigger : T, settings: WebhookSettings, ziqniContext: ZiqniContext, ziqniWebhookTransformer: ZiqniWebhookTransformer = defaultWebhookTransformer): Unit = trigger match {

		case t: onNewProductTrigger => ziqniWebhookTransformer.onNewProduct(settings, t.productId, ziqniContext)

		case t: onNewMemberTrigger => ziqniWebhookTransformer.onNewMember(settings, t.memberId, ziqniContext)

		case t: onCompetitionCreatedTrigger => ziqniWebhookTransformer.onCompetitionCreated(settings, t.competitionId, ziqniContext)

		case t: onCompetitionStartedTrigger => ziqniWebhookTransformer.onCompetitionStarted(settings, t.competitionId, ziqniContext)

		case t: onCompetitionFinishedTrigger => ziqniWebhookTransformer.onCompetitionFinished(settings, t.competitionId, ziqniContext)

		case t: onCompetitionCancelledTrigger => ziqniWebhookTransformer.onCompetitionCancelled(settings, t.competitionId, ziqniContext)

		case t: onCompetitionRewardIssuedTrigger => ziqniWebhookTransformer.onCompetitionRewardIssued(settings, t.competitionId, t.memberId, t.awardId, t.rewardTypeKey, ziqniContext)

		case t: onContestCreatedTrigger => ziqniWebhookTransformer.onContestCreated(settings, t.contestId, ziqniContext)

		case t: onContestStartedTrigger => ziqniWebhookTransformer.onContestStarted(settings, t.contestId, ziqniContext)

		case t: onContestFinishedTrigger => ziqniWebhookTransformer.onContestFinished(settings, t.contestId, ziqniContext)

		case t: onContestFinalisedTrigger => ziqniWebhookTransformer.onContestFinalised(settings, t.contestId, ziqniContext)

		case t: onContestCancelledTrigger => ziqniWebhookTransformer.onContestCancelled(settings, t.contestId, ziqniContext)

		case t: onContestRewardCreatedTrigger => ziqniWebhookTransformer.onContestRewardCreated(settings, t.rewardId, ziqniContext)

		case t: onContestRewardIssuedTrigger => ziqniWebhookTransformer.onContestRewardIssued(settings, t.contestId, t.memberId, t.awardId, t.rewardTypeKey, ziqniContext)

		case t: onContestRewardClaimedTrigger => ziqniWebhookTransformer.onContestRewardClaimed(settings, t.contestId, t.memberId, t.awardId, t.rewardTypeKey, ziqniContext)

		case t: onAchievementCreatedTrigger => ziqniWebhookTransformer.onAchievementCreated(settings, t.achievementId, ziqniContext)

		case t: onAchievementTriggeredTrigger => ziqniWebhookTransformer.onAchievementTriggered(settings, t.achievementId, t.memberId, ziqniContext)

		case t: onAchievementRewardCreatedTrigger => ziqniWebhookTransformer.onAchievementRewardCreated(settings, t.rewardId, ziqniContext)
			
		case t: onAchievementRewardIssuedTrigger => ziqniWebhookTransformer.onAchievementRewardIssued(settings, t.achievementId, t.memberId, t.awardId, t.rewardTypeKey, ziqniContext)

		case t: onAchievementRewardClaimedTrigger => ziqniWebhookTransformer.onAchievementRewardClaimed(settings, t.achievementId, t.memberId, t.awardId, t.rewardTypeKey, ziqniContext)

	}
}
