package io.omnitrade.auth

import io.omnitrade.di.Dependencies
import io.omnitrade.ext.toHmacSha256
import io.omnitrade.ext.toQueryString
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

internal class Auth(
  private val accessKey: String = "",
  private val secretKey: String = "",
  private val dependencies: Kodein = Kodein { import(Dependencies.authModule) }
) {
  fun signedParams(verb: String, path: String, params: MutableMap<String, String>) =
    sign(verb, path, params)

  fun hasKeys(): Boolean = accessKey.isNotEmpty() && secretKey.isNotEmpty()

  private fun sign(verb: String, path: String, params: MutableMap<String, String>) =
    payload(verb, path, params).toHmacSha256(secretKey)

  private fun payload(verb: String, path: String, params: MutableMap<String, String>) =
    "$verb|$path|${formatParams(params)}"

  private fun formatParams(params: MutableMap<String, String>): String {
    params["access_key"] = this.accessKey
    val currentTime: String by dependencies.instance(tag = "currentTime")
    params["tonce"] = currentTime
    return params.toSortedMap().toQueryString()
  }
}
