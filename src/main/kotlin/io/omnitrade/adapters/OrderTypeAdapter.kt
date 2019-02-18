package io.omnitrade.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import io.omnitrade.enumerator.order.OrderType
import java.lang.reflect.Type

internal class OrderTypeAdapter: JsonDeserializer<OrderType> {
  override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
    OrderType.values().firstOrNull { it.type == json?.asString }
}
