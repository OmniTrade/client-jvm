package io.omnitrade.model

data class Account(
  val currency: String,
  val balance: Double,
  val locked: Double
)