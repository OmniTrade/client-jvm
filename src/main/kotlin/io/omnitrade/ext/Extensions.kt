package io.omnitrade.ext

import com.google.common.base.Joiner
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun String.toHmacSha256(secretKey: String): String {
  val algorithm = "HmacSHA256"
  val message = this
  val hash = Mac.getInstance(algorithm).run {
    init(SecretKeySpec(secretKey.toByteArray(charset("UTF8")), algorithm))
    doFinal(message.toByteArray(charset("UTF8")))
  }

  return hash.fold(StringBuilder()) { acc, next ->
    acc.append(String.format("%02x", next))
  }.toString().toLowerCase()
}

fun MutableMap<String, String>.toQueryString(): String {
  return Joiner.on("&").withKeyValueSeparator("=").join(this)
}

fun List<*>.toQueryStringArray(): String {
  return this.toString()
    .toLowerCase()
    .replace("{", "[")
    .replace("}", "]")
    .replace(" ", "")
}

