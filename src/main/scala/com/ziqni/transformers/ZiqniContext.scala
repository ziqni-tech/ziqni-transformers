package com.ziqni.transformers

import scala.concurrent.ExecutionContextExecutor

object ZiqniContext {
  case class ZiqniContextHolder[T](ziqniContext: ZiqniContext, value:T)
}

case class ZiqniContext(ziqniTransformerInfo: ZiqniTransformerInfo,
                        ziqniApi: ZiqniApi,
                        ziqniApiAsync: ZiqniApiAsync,
                        ziqniTransformerEventBus:Option[ZiqniTransformerEventBus],
                        transformerExecutionContext: ExecutionContextExecutor)
