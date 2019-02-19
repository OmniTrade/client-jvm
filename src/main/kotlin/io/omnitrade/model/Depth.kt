package io.omnitrade.model

data class Depth(
  val timestamp: Long,
  val asks: List<List<String>>,
  val bids: List<List<String>>
)
