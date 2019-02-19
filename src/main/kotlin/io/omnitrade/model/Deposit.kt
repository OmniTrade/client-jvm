package io.omnitrade.model

import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Deposit(
  val id: Int,
  val currency: String,
  val amount: Double,
  val fee: Double,
  val txid: Double,
  @SerializedName("created_at") val createdAt: Instant,
  val confirmations: String,
  @SerializedName("done_at") val doneAt: Instant,
  val state: String
)
