/***
  *  Copyright (C) Ziqni Ltd - All Rights Reserved
  *  Unauthorized copying of this file, via any medium is strictly prohibited
  *  Proprietary and confidential
  *  Written by Ziqni Ltd, 2021
  */
package example.transformers

import com.ziqni.transformers.domain.WebhookSettings
import com.ziqni.transformers.{ZiqniWebhookTransformer, ZiqniApi}
import org.joda.time.DateTime

class ExampleWebhookTransformer extends ZiqniWebhookTransformer {

	override def onNewProduct(settings: WebhookSettings, productId: String, ziqniApi: ZiqniApi): Unit = {
		val body = Map[String, Any](
			"accountId" -> ziqniApi.accountId,
			"productId" -> productId,
			"productRefId" -> ziqniApi.productRefIdFromProductId(productId),
			"resourcePath" -> s"products/$productId",
			"timestamp" -> DateTime.now().getMillis
		)

		val json =  ziqniApi.toJsonFromMap(body)
		val headers = settings.headers ++ ziqniApi.HTTPDefaultHeader()

		ziqniApi.httpPost(settings.url, json, headers)
	}
}
