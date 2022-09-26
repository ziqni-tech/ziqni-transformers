/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package com.ziqni.transformers

import com.ziqni.transformers.domain.WebhookSettings

/**
  * Deprecated. Use the system notify mechanism for receiving system changes
  */
@Deprecated(since ="On deprecation path")
trait ZiqniWebhookTransformer {

  /**
    * Executed when a new product is registered in your Ziqni space
    * @param settings The user supplied settings
    * @param productId The Ziqni product id
    * @param ziqniApi The Ziqni API
    */
  def onNewProduct(settings: WebhookSettings, productId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a new member is registered in your Ziqni space
    * @param settings The user supplied settings
    * @param memberId The Ziqni member id
    * @param ziqniApi The Ziqni API
    */
  def onNewMember(settings: WebhookSettings, memberId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a new competition is created in your Ziqni space
    * @param settings The user supplied settings
    * @param competitionId The Ziqni competition id
    * @param ziqniApi The Ziqni API
    */
  def onCompetitionCreated(settings: WebhookSettings, competitionId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a competition is started
    * @param settings The user supplied settings
    * @param competitionId The Ziqni competition id
    * @param ziqniApi The Ziqni API
    */
  def onCompetitionStarted(settings: WebhookSettings, competitionId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a competition finished
    * @param settings The user supplied settings
    * @param competitionId The Ziqni competition id
    * @param ziqniApi The Ziqni API
    */
  def onCompetitionFinished(settings: WebhookSettings, competitionId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a competition is cancelled
    * @param settings The user supplied settings
    * @param competitionId The Ziqni competition id
    * @param ziqniApi The Ziqni API
    */
  def onCompetitionCancelled(settings: WebhookSettings, competitionId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a competition is cancelled
    * @param settings The user supplied settings
    * @param competitionId The Ziqni competition id
    * @param memberId The Ziqni member id
    * @param awardId The Ziqni award id
    * @param rewardTypeKey The user defined reward type key
    * @param ziqniApi The Ziqni API
    */
  def onCompetitionRewardIssued(settings: WebhookSettings, competitionId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a new contest is created in your Ziqni space
    * @param settings The user supplied settings
    * @param contestId The Ziqni contest id
    * @param ziqniApi The Ziqni API
    */
  def onContestCreated(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a contest is started
    * @param settings The user supplied settings
    * @param contestId The Ziqni contest id
    * @param ziqniApi The Ziqni API
    */
  def onContestStarted(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a contest finished
    * @param settings The user supplied settings
    * @param contestId The Ziqni contest id
    * @param ziqniApi The Ziqni API
    */
  def onContestFinished(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a contest is finalised
    * @param settings The user supplied settings
    * @param contestId The Ziqni contest id
    * @param ziqniApi The Ziqni API
    */
  def onContestFinalised(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a contest is cancelled
    * @param settings The user supplied settings
    * @param contestId The Ziqni contest id
    * @param ziqniApi The Ziqni API
    */
  def onContestCancelled(settings: WebhookSettings, contestId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a new reward is created in your Ziqni space
    * @param settings The user supplied settings
    * @param rewardId The Ziqni reward id
    * @param ziqniApi The Ziqni API
    */
  def onContestRewardCreated(settings: WebhookSettings, rewardId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a contest finished
    * @param settings The user supplied settings
    * @param contestId The Ziqni contest id
    * @param memberId The Ziqni member id
    * @param awardId The Ziqni award id
    * @param rewardTypeKey The user defined reward type key
    * @param ziqniApi The Ziqni API
    */
  def onContestRewardIssued(settings: WebhookSettings, contestId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {}


  /**
    * Executed when a contest reward is claimed
    * @param settings The user supplied settings
    * @param contestId The Ziqni contest id
    * @param memberId The Ziqni member id
    * @param awardId The Ziqni award id
    * @param rewardTypeKey The user defined reward type key
    * @param ziqniApi The Ziqni API
    */
  def onContestRewardClaimed(settings: WebhookSettings, contestId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a new contest is created in your Ziqni space
    * @param settings The user supplied settings
    * @param achievementId The Ziqni achievement id
    * @param ziqniApi The Ziqni API
    */
  def onAchievementCreated(settings: WebhookSettings, achievementId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when an achievement is triggered
    * @param settings The user supplied settings
    * @param achievementId The Ziqni achievement id
    * @param memberId The Ziqni member id
    * @param ziqniApi The Ziqni API
    */
  def onAchievementTriggered(settings: WebhookSettings, achievementId: String, memberId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a new reward is created in your Ziqni space
    * @param settings The user supplied settings
    * @param rewardId The Ziqni reward id
    * @param ziqniApi The Ziqni API
    */
  def onAchievementRewardCreated(settings: WebhookSettings, rewardId: String, ziqniContext: ZiqniContext): Unit = {}

  /**
    * Executed when a reward was awarded to a member
    * @param settings The user supplied settings
    * @param achievementId The Ziqni achievement id
    * @param memberId The Ziqni member id
    * @param awardId The Ziqni award id
    * @param rewardTypeKey The user defined reward type key
    * @param ziqniApi The Ziqni API
    */
  def onAchievementRewardIssued(settings: WebhookSettings, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {}


  /**
    * Executed when a reward was claimed by a member
    * @param settings The user supplied settings
    * @param achievementId The Ziqni achievement id
    * @param memberId The Ziqni member id
    * @param awardId The Ziqni award id
    * @param rewardTypeKey The user defined reward type key
    * @param ziqniApi The Ziqni API
    */
  def onAchievementRewardClaimed(settings: WebhookSettings, achievementId: String, memberId: String, awardId: String, rewardTypeKey: String, ziqniContext: ZiqniContext): Unit = {}
}
