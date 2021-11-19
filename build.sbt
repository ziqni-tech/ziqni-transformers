/***
  *  Copyright (C) Ziqni Ltd, Inc - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
 */

/*** When developing locally do not modify this file ***/
name := "ziqni-transformers"

organization := "com.ziqni"

version := "1.0"

isSnapshot := true

scalaVersion := "2.13.7"

resolvers += Resolver.mavenLocal

val json4sVersion = "4.0.3"
val scalaTestVersion = "3.2.8"
val avroVersion = "1.11.0"

libraryDependencies += "org.json4s" %% "json4s-ext" % json4sVersion
libraryDependencies += "org.json4s" %% "json4s-native" % json4sVersion
libraryDependencies += "org.json4s" %% "json4s-jackson" % json4sVersion
libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % Test
libraryDependencies += "org.scalatest" %% "scalatest-funsuite" % scalaTestVersion % Test
libraryDependencies += "org.apache.avro" % "avro" % avroVersion

