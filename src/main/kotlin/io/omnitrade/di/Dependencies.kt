package io.omnitrade.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.omnitrade.adapters.*
import io.omnitrade.auth.Auth
import io.omnitrade.enumerator.order.OrderDepth
import io.omnitrade.enumerator.order.OrderSide
import io.omnitrade.enumerator.order.OrderState
import io.omnitrade.enumerator.order.OrderType
import io.omnitrade.repository.OmnitradeRepository
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.provider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import java.util.concurrent.TimeUnit

internal object Dependencies {
  val clientModule = Kodein.Module("clientModule") {
    bind<Auth>() with factory { accessKey: String, secretKey: String ->
      Auth(accessKey, secretKey)
    }

    val gson = GsonBuilder()
      .registerTypeAdapter(Instant::class.java, InstantAdapter())
      .registerTypeAdapter(OrderState::class.java, OrderStateAdapter())
      .registerTypeAdapter(OrderType::class.java, OrderTypeAdapter())
      .registerTypeAdapter(OrderSide::class.java, OrderSideAdapter())
      .registerTypeAdapter(OrderDepth::class.java, OrderDepthAdapter())
      .enableComplexMapKeySerialization()
      .serializeNulls()
      .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
      .setPrettyPrinting()
      .create()

    bind<OmnitradeRepository>() with factory { endpoint: String, timeout: Long ->
      val okHttp = OkHttpClient.Builder()
          .connectTimeout(timeout, TimeUnit.SECONDS)
          .readTimeout(timeout, TimeUnit.SECONDS)
          .build()

      val retrofit = Retrofit.Builder()
        .baseUrl(endpoint)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

      retrofit.create(OmnitradeRepository::class.java)
    }
  }

  val authModule = Kodein.Module("authModule") {
    bind(tag = "currentTime") from provider {
      System.currentTimeMillis().toString()
    }
  }
}
