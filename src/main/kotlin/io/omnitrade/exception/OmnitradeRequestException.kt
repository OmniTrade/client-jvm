package io.omnitrade.exception

class OmnitradeRequestException(
  val statusCode: String,
  val reason: String
): Exception("Your request has failed: StatusCode=$statusCode, Reason=$reason")
