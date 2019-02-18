package io.omnitrade.model

import com.google.gson.annotations.SerializedName
import io.omnitrade.enumerator.order.OrderSide
import io.omnitrade.enumerator.order.OrderState
import io.omnitrade.enumerator.order.OrderType
import java.math.BigDecimal
import java.time.Instant

data class Order(
  val id: Int,
  val side: OrderSide,
  @SerializedName("ord_type") val ordType: OrderType,
  val price: Double,
  @SerializedName("avg_price") val avgPrice: Double,
  val state: OrderState,
  val market: String,
  @SerializedName("created_at") val createdAt: Instant,
  val volume: BigDecimal,
  @SerializedName("remaining_volume") val remainingVolume: BigDecimal,
  @SerializedName("executed_volume") val executedVolume: BigDecimal,
  @SerializedName("trades_count") val tradesCount: Int,
  val trades: List<Trade>
)
