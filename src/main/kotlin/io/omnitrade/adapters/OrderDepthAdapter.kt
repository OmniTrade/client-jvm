package io.omnitrade.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import io.omnitrade.enumerator.order.OrderDepth
import java.lang.reflect.Type

internal class OrderDepthAdapter: JsonDeserializer<OrderDepth> {
  override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
    OrderDepth.values().firstOrNull { it.depth == json?.asString }
}
