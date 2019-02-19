package io.omnitrade.repository

import io.omnitrade.model.*
import retrofit2.http.*
import java.util.concurrent.CompletableFuture

internal interface OmnitradeRepository {
  companion object {
    private const val API_VERSION = "/api/v2"
  }

  @GET("$API_VERSION/markets")
  fun getMarkets(): CompletableFuture<List<Market>>

  @GET("$API_VERSION/members/me")
  fun getMe(@QueryMap options: MutableMap<String, String>): CompletableFuture<Member>

  @GET("$API_VERSION/deposits")
  fun getDeposits(@QueryMap options: MutableMap<String, String>): CompletableFuture<List<Deposit>>

  @GET("$API_VERSION/deposit")
  fun getDeposit(@QueryMap options: MutableMap<String, String>): CompletableFuture<Deposit>

  @GET("$API_VERSION/deposit_address")
  fun getDepositAddress(@QueryMap options: MutableMap<String, String>): CompletableFuture<DepositAddress>

  @GET("$API_VERSION/orders")
  fun getOrders(@QueryMap options: MutableMap<String, String>): CompletableFuture<List<Order>>

  @POST("$API_VERSION/orders")
  fun createOrder(@QueryMap options: MutableMap<String, String>): CompletableFuture<Order>

  @POST("$API_VERSION/orders/multi")
  @FormUrlEncoded
  fun createMultipleOrders(@FieldMap options: MutableMap<String, String>): CompletableFuture<List<Order>>

  @POST("$API_VERSION/orders/clear")
  fun clearAllOrders(@QueryMap options: MutableMap<String, String>): CompletableFuture<List<Order>>

  @POST("$API_VERSION/orders/clear_bottom")
  fun clearBottomOrder(@QueryMap options: MutableMap<String, String>): CompletableFuture<List<Order>>

  @GET("$API_VERSION/order")
  fun getOrder(@QueryMap options: MutableMap<String, String>): CompletableFuture<Order>

  @POST("$API_VERSION/order/delete")
  fun deleteOrder(@QueryMap options: MutableMap<String, String>): CompletableFuture<Order>

  @GET("$API_VERSION/order_book")
  fun getOrderBook(@QueryMap options: MutableMap<String, String>): CompletableFuture<OrderBook>

  @GET("$API_VERSION/depth")
  fun getDepth(@QueryMap options: MutableMap<String, String>): CompletableFuture<Depth>

  @GET("$API_VERSION/order_markets")
  fun getOrderMarket(@QueryMap options: MutableMap<String, String>): CompletableFuture<OrderMarket>

  @GET("$API_VERSION/trades")
  fun getTrades(@QueryMap options: MutableMap<String, String>): CompletableFuture<List<Trade>>

  @GET("$API_VERSION/trades/my")
  fun getMyTrades(@QueryMap options: MutableMap<String, String>): CompletableFuture<List<Trade>>

  @GET("$API_VERSION/k")
  fun getK(@QueryMap options: MutableMap<String, String>): CompletableFuture<List<List<Int>>>

  @GET("$API_VERSION/k_with_pending_trades")
  fun getKWithPendingTrades(@QueryMap options: MutableMap<String, String>): CompletableFuture<KWithPendingTrade>

}
