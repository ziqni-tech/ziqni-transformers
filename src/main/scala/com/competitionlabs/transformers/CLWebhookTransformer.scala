/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
  */
package com.competitionlabs.transformers

import com.competitionlabs.transformers.domain.WebhookSettings

trait CLWebhookTransformer {

  /**
    * Executed when a new product is registered in your CompetitionLabs space
    * @param settings The user supplied settings
    * @param productId The CompetitionLabs product id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onNewProduct(settings: WebhookSettings, productId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a new member is registered in your CompetitionLabs space
    * @param settings The user supplied settings
    * @param memberId The CompetitionLabs member id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onNewMember(settings: WebhookSettings, memberId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a new competition is created in your CompetitionLabs space
    * @param settings The user supplied settings
    * @param competitionId The CompetitionLabs competition id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onCompetitionCreated(settings: WebhookSettings, competitionId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a competition is started
    * @param settings The user supplied settings
    * @param competitionId The CompetitionLabs competition id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onCompetitionStarted(settings: WebhookSettings, competitionId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a competition finished
    * @param settings The user supplied settings
    * @param competitionId The CompetitionLabs competition id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onCompetitionFinished(settings: WebhookSettings, competitionId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a competition is cancelled
    * @param settings The user supplied settings
    * @param competitionId The CompetitionLabs competition id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onCompetitionCancelled(settings: WebhookSettings, competitionId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a competition is cancelled
    * @param settings The user supplied settings
    * @param competitionId The CompetitionLabs competition id
    * @param memberId The CompetitionLabs member id
    * @param awardId The CompetitionLabs award id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onCompetitionRewardIssued(settings: WebhookSettings, competitionId: String, memberId: String, awardId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a new contest is created in your CompetitionLabs space
    * @param settings The user supplied settings
    * @param contestId The CompetitionLabs contest id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onContestCreated(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a contest is started
    * @param settings The user supplied settings
    * @param contestId The CompetitionLabs contest id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onContestStarted(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a contest finished
    * @param settings The user supplied settings
    * @param contestId The CompetitionLabs contest id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onContestFinished(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a contest is finalised
    * @param settings The user supplied settings
    * @param contestId The CompetitionLabs contest id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onContestFinalised(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a contest is cancelled
    * @param settings The user supplied settings
    * @param contestId The CompetitionLabs contest id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onContestCancelled(settings: WebhookSettings, contestId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a contest finished
    * @param settings The user supplied settings
    * @param contestId The CompetitionLabs contest id
    * @param memberId The CompetitionLabs member id
    * @param awardId The CompetitionLabs award id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onContestRewardIssued(settings: WebhookSettings, contestId: String, memberId: String, awardId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a new contest is created in your CompetitionLabs space
    * @param settings The user supplied settings
    * @param achievementId The CompetitionLabs achievement id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onAchievementCreated(settings: WebhookSettings, achievementId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when an achievement is triggered
    * @param settings The user supplied settings
    * @param achievementId The CompetitionLabs achievement id
    * @param memberId The CompetitionLabs member id
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onAchievementTriggered(settings: WebhookSettings, achievementId: String, memberId: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}

  /**
    * Executed when a reward was awarded to a member
    * @param settings The user supplied settings
    * @param achievementId The CompetitionLabs achievement id
    * @param memberId The CompetitionLabs member id
    * @param rewardId The CompetitionLabs reward id
    * @param awardId The CompetitionLabs award id
    * @param rewardTypeKey The user defined reward type key
    * @param competitionLabsApi The CompetitionLabs API
    */
  def onAchievementRewardIssued(settings: WebhookSettings, achievementId: String, memberId: String, rewardId: String, awardId: String, rewardTypeKey: String, competitionLabsApi: CompetitionLabsApiExt): Unit = {}
}
