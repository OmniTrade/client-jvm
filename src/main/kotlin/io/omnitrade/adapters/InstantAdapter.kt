package io.omnitrade.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.Instant
import java.time.OffsetDateTime

internal class InstantAdapter: JsonDeserializer<Instant> {
  override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) = try {
    OffsetDateTime.parse(json?.asString).toInstant()
  } catch (ex: Exception) {
    null
  }
}
