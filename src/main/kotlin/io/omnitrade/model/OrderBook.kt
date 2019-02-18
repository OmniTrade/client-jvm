package io.omnitrade.model

data class OrderBook(
  val asks: List<Order>,
  val bids: List<Order>
)
