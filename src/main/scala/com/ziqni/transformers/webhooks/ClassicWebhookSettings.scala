/***
 *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Ziqni Ltd, 2023
 */

package com.ziqni.transformers.webhooks

import com.ziqni.transformers.domain.BasicEntityChangeSubscriptionRequest
import com.ziqni.transformers.webhooks.ClassicWebhookSettings._
import scala.collection.mutable.ListBuffer

object ClassicWebhookSettings {
	private val EntityChanged = "EntityChanged"
	private val EntityStateChanged = "EntityStateChanged"
	val TYPE_OF_CHANGE_CREATED = 1;
	val TYPE_OF_CHANGE_UPDATED = 2;
	val TYPE_OF_CHANGE_DELETED = 3;
	val ParentType = "parentType"
	val Claimed = "claimed"
	val Unknown = "Unknown"
	val Competition = "Competition"
	val Contest = "Contest"
	val Product = "Product"
	val Member = "Member"
	val Achievement = "Achievement"
	val Reward = "Reward"
	val Award = "Award"
}

/**
 * Settings for classic webhooks
 * The onAchievementTriggered has been deprecated and no proxy exists for it
 * @param url The endpoint to post the message to
 * @param headers The headers to add to the post message
 * @param onNewProductEnabled Executed when a new product is registered in your ZIQNI space.
 * @param onNewMemberEnabled Executed when a new member is created in your ZIQNI space.
 * @param onCompetitionCreatedEnabled Executed when a new competition is created in your ZIQNI space
 * @param onCompetitionStartedEnabled Executed when a competition is started
 * @param onCompetitionFinishedEnabled Executed when a competition is finished.
 * @param onCompetitionCancelledEnabled Executed when a competition is cancelled.
 * @param onCompetitionRewardIssuedEnabled Executed when a reward for a competition is issued.
 * @param onContestCreatedEnabled Executed when a new contest is created in your ZIQNI  space
 * @param onContestStartedEnabled Executed when a new contest is started.
 * @param onContestFinishedEnabled Executed when a new contest is finished.
 * @param onContestFinalisedEnabled Executed when a new contest is finalised.
 * @param onContestCancelledEnabled Executed when a new contest is cancelled.
 * @param onContestRewardCreatedEnabled Executed when a reward for a contest is created.
 * @param onContestRewardIssuedEnabled Executed when a reward for a contest is issued.
 * @param onContestRewardClaimedEnabled Executed when a reward for a contest is claimed
 * @param onAchievementCreatedEnabled Executed when a new achievement is created in your ZIQNI space.
 * @param onAchievementRewardCreatedEnabled Executed when a reward was created in the achievement.
 * @param onAchievementRewardIssuedEnabled Executed when a reward was awarded to a member.
 * @param onAchievementRewardClaimedEnabled Executed when a reward was claimed by a member
 */
case class ClassicWebhookSettings(
																	 url: String,
																	 headers: Map[String, Seq[String]] = Map.empty,
																	 onNewProductEnabled: Boolean = false,
																	 onNewMemberEnabled: Boolean = false,
																	 onCompetitionCreatedEnabled: Boolean = false,
																	 onCompetitionStartedEnabled: Boolean = false,
																	 onCompetitionFinishedEnabled: Boolean = false,
																	 onCompetitionCancelledEnabled: Boolean = false,
																	 onCompetitionRewardIssuedEnabled: Boolean = false,
																	 onContestCreatedEnabled: Boolean = false,
																	 onContestStartedEnabled: Boolean = false,
																	 onContestFinishedEnabled: Boolean = false,
																	 onContestFinalisedEnabled: Boolean = false,
																	 onContestCancelledEnabled: Boolean = false,
																	 onContestRewardCreatedEnabled: Boolean = false,
																	 onContestRewardIssuedEnabled: Boolean = false,
																	 onContestRewardClaimedEnabled: Boolean = false,
																	 onAchievementCreatedEnabled: Boolean = false,
																	 onAchievementRewardCreatedEnabled: Boolean = false,
																	 onAchievementRewardIssuedEnabled: Boolean = false,
																	 onAchievementRewardClaimedEnabled: Boolean = false
																 ){

	/**
	 * The required entity change and state change subscriptions based on the selected settings
	 */
	val classicEntityChangeSubscriptionRequest: Seq[BasicEntityChangeSubscriptionRequest] = {

		val list = new ListBuffer[BasicEntityChangeSubscriptionRequest]()

		if(onNewProductEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityChanged, Product)

		if(onNewMemberEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityChanged, Member)

		if(onCompetitionCreatedEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityChanged, Competition)

		if(onCompetitionStartedEnabled || onCompetitionFinishedEnabled || onCompetitionCancelledEnabled || onCompetitionCancelledEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityStateChanged, Competition)

		if (onContestCreatedEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityChanged, Contest)

		if(onContestStartedEnabled || onContestFinishedEnabled || onContestFinalisedEnabled || onContestCancelledEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityStateChanged, Contest)

		if (onCompetitionRewardIssuedEnabled || onContestRewardIssuedEnabled || onAchievementRewardIssuedEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityChanged, Award)

		if(onContestRewardCreatedEnabled || onAchievementRewardCreatedEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityChanged, Reward)

		if(onContestRewardClaimedEnabled || onAchievementRewardClaimedEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityStateChanged, Award)

		if(onAchievementCreatedEnabled)
			list += BasicEntityChangeSubscriptionRequest(EntityChanged, Achievement)

		list.toSeq
	}
}
