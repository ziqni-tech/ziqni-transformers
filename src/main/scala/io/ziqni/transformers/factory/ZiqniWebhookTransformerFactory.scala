/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package io.ziqni.transformers.factory

import io.ziqni.transformers.domain._
import io.ziqni.transformers.domain.WebhookSettings
import io.ziqni.transformers.impl.DefaultWebhookTransformer
import io.ziqni.transformers.{ZiqniWebhookTransformer, ZiqniApi}

object ZiqniWebhookTransformerFactory {

	val defaultWebhookTransformer = new DefaultWebhookTransformer

	def apply[T <: WebhookTransformerTrigger](trigger : T, settings: WebhookSettings, ziqniApi: ZiqniApi, ziqniWebhookTransformer: ZiqniWebhookTransformer = defaultWebhookTransformer): Unit = trigger match {

		case t: onNewProductTrigger => ziqniWebhookTransformer.onNewProduct(settings, t.productId, ziqniApi)

		case t: onNewMemberTrigger => ziqniWebhookTransformer.onNewMember(settings, t.memberId, ziqniApi)

		case t: onCompetitionCreatedTrigger => ziqniWebhookTransformer.onCompetitionCreated(settings, t.competitionId, ziqniApi)

		case t: onCompetitionStartedTrigger => ziqniWebhookTransformer.onCompetitionStarted(settings, t.competitionId, ziqniApi)

		case t: onCompetitionFinishedTrigger => ziqniWebhookTransformer.onCompetitionFinished(settings, t.competitionId, ziqniApi)

		case t: onCompetitionCancelledTrigger => ziqniWebhookTransformer.onCompetitionCancelled(settings, t.competitionId, ziqniApi)

		case t: onCompetitionRewardIssuedTrigger => ziqniWebhookTransformer.onCompetitionRewardIssued(settings, t.competitionId, t.memberId, t.awardId, t.rewardTypeKey, ziqniApi)

		case t: onContestCreatedTrigger => ziqniWebhookTransformer.onContestCreated(settings, t.contestId, ziqniApi)

		case t: onContestStartedTrigger => ziqniWebhookTransformer.onContestStarted(settings, t.contestId, ziqniApi)

		case t: onContestFinishedTrigger => ziqniWebhookTransformer.onContestFinished(settings, t.contestId, ziqniApi)

		case t: onContestFinalisedTrigger => ziqniWebhookTransformer.onContestFinalised(settings, t.contestId, ziqniApi)

		case t: onContestCancelledTrigger => ziqniWebhookTransformer.onContestCancelled(settings, t.contestId, ziqniApi)

		case t: onContestRewardCreatedTrigger => ziqniWebhookTransformer.onContestRewardCreated(settings, t.rewardId, ziqniApi)

		case t: onContestRewardIssuedTrigger => ziqniWebhookTransformer.onContestRewardIssued(settings, t.contestId, t.memberId, t.awardId, t.rewardTypeKey, ziqniApi)

		case t: onContestRewardClaimedTrigger => ziqniWebhookTransformer.onContestRewardClaimed(settings, t.contestId, t.memberId, t.awardId, t.rewardTypeKey, ziqniApi)

		case t: onAchievementCreatedTrigger => ziqniWebhookTransformer.onAchievementCreated(settings, t.achievementId, ziqniApi)

		case t: onAchievementTriggeredTrigger => ziqniWebhookTransformer.onAchievementTriggered(settings, t.achievementId, t.memberId, ziqniApi)

		case t: onAchievementRewardCreatedTrigger => ziqniWebhookTransformer.onAchievementRewardCreated(settings, t.rewardId, ziqniApi)
			
		case t: onAchievementRewardIssuedTrigger => ziqniWebhookTransformer.onAchievementRewardIssued(settings, t.achievementId, t.memberId, t.awardId, t.rewardTypeKey, ziqniApi)

		case t: onAchievementRewardClaimedTrigger => ziqniWebhookTransformer.onAchievementRewardClaimed(settings, t.achievementId, t.memberId, t.awardId, t.rewardTypeKey, ziqniApi)

	}
}
