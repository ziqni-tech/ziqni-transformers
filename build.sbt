/***
  *  Copyright (C) Competition Labs Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2017
 */

/*** When developing locally do not modify this file ***/
name := "competitionlabs-transformers"

organization := "com.competitionlabs"

version := "1.4"

isSnapshot := true

scalaVersion := "2.11.11"

resolvers += Resolver.mavenLocal

val json4sVersion = "3.5.2"

libraryDependencies ++= Seq(
	"org.json4s" % "json4s-ext_2.11" % json4sVersion,
	"org.json4s" % "json4s-native_2.11" % json4sVersion,
	"org.json4s" % "json4s-jackson_2.11" % json4sVersion,
	"org.scalatest" %% "scalatest" % "2.2.1" % "test",
	"org.scalatestplus" %% "play" % "1.4.0-M3" % "test"
)