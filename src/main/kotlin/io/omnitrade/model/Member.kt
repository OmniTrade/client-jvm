package io.omnitrade.model

data class Member(
  val sn: String,
  val name: String,
  val email: String,
  val activated: Boolean,
  val accounts : List<Account>
)