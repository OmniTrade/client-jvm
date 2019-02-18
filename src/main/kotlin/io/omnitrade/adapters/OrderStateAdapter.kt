package io.omnitrade.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import io.omnitrade.enumerator.order.OrderState
import java.lang.reflect.Type

internal class OrderStateAdapter: JsonDeserializer<OrderState> {
  override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
    OrderState.values().firstOrNull { it.state == json?.asString }
}
