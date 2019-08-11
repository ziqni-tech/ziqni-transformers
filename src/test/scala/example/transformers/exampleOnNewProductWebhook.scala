/***
  *  Copyright (C) Competition Labs Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Competition Labs Ltd, 2019
  */
package example.transformers

import com.competitionlabs.transformers.domain.WebhookSettings
import com.competitionlabs.transformers.{CLWebhookTransformer, CompetitionLabsApiExt}
import org.joda.time.DateTime

class exampleOnNewProductWebhook extends CLWebhookTransformer {

	override def onNewProduct(settings: WebhookSettings, productId: String, competitionLabsApi: CompetitionLabsApiExt) = {
		val body = Map[String, Any](
			"accountId" -> competitionLabsApi.accountId,
			"productId" -> productId,
			"productRefId" -> competitionLabsApi.productRefIdFromProductId(productId),
			"resourcePath" -> s"/api/${competitionLabsApi.spaceName}/products/$productId",
			"timestamp" -> DateTime.now().getMillis
		)

		val json =  competitionLabsApi.toJsonFromMap(body)
		val headers = settings.headers ++ competitionLabsApi.HTTPDefaultHeader()

		competitionLabsApi.httpPost(settings.url, json, headers)
	}
}
