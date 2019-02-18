package io.omnitrade.enumerator.order

enum class OrderState(val state: String) {
  WAIT("wait"),
  DONE("done"),
  CANCEL("cancel")
}
