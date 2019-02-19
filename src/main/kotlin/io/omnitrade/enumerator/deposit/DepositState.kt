package io.omnitrade.enumerator.deposit

enum class DepositState(val state: String) {
  SUBMITTING("submitting"),
  CANCELLED("cancelled"),
  SUBMITTED("submitted"),
  REJECTED("rejected"),
  ACCEPTED("accepted"),
  CHECKED("checked"),
  WARNING("warning")
}
