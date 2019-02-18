package io.omnitrade.model

import com.google.gson.annotations.SerializedName
import io.omnitrade.enumerator.order.OrderSide
import java.math.BigDecimal
import java.time.Instant

data class Trade(
  val tid: Int,
  val price: Double,
  val amount: BigDecimal,
  val funds: Double,
  val market: String,
  @SerializedName("created_at") val createdAt: Instant,
  val date: Long,
  val side: OrderSide
)
