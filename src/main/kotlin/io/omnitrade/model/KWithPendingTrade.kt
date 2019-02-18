package io.omnitrade.model

data class KWithPendingTrade(
  val k: List<Int>,
  val trades: List<Trade>
)
