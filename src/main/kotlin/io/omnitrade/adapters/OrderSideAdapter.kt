package io.omnitrade.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import io.omnitrade.enumerator.order.OrderSide
import java.lang.reflect.Type

internal class OrderSideAdapter: JsonDeserializer<OrderSide> {
  override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
    OrderSide.values().firstOrNull { it.side == json?.asString }
}
