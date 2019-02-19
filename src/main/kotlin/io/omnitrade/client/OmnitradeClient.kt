package io.omnitrade.client

import io.omnitrade.auth.Auth
import io.omnitrade.di.Dependencies
import io.omnitrade.enumerator.OrderBy
import io.omnitrade.enumerator.deposit.DepositState
import io.omnitrade.enumerator.http.HttpMethod
import io.omnitrade.enumerator.k.Period
import io.omnitrade.enumerator.market.MarketType
import io.omnitrade.enumerator.order.OrderDepth
import io.omnitrade.enumerator.order.OrderSide
import io.omnitrade.enumerator.order.OrderState
import io.omnitrade.enumerator.order.OrderType
import io.omnitrade.exception.OmnitradeNoAuthKeysException
import io.omnitrade.exception.OmnitradeRequestException
import io.omnitrade.ext.toQueryStringArray
import io.omnitrade.model.*
import io.omnitrade.repository.OmnitradeRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.M
import org.kodein.di.generic.instance
import retrofit2.HttpException
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.util.concurrent.CompletableFuture

class OmnitradeClient private constructor(
  private var accessKey: String = "",
  private var secretKey: String = "",
  endpoint: String = DEFAULT_ENDPOINT,
  timeout: Long = 60,
  dependencies: Kodein = Kodein { import(Dependencies.clientModule) }
) {
  companion object {
    private const val API_VERSION = "/api/v2"
    private const val DEFAULT_ENDPOINT = "https://omnitrade.io/"
  }

  object Builder {
    private var accessKey: String = ""
    private var secretKey: String = ""
    private var endpoint: String = DEFAULT_ENDPOINT
    private var timeout: Long = 60

    fun build(): OmnitradeClient {
      return OmnitradeClient(accessKey, secretKey, endpoint, timeout)
    }

    internal fun build(dependencies: Kodein): OmnitradeClient {
      return OmnitradeClient(accessKey, secretKey, endpoint, timeout, dependencies)
    }

    @JvmName("setAccessKey")
    fun accessKey(accessKey: String): Builder {
      Builder.accessKey = accessKey
      return this
    }

    @JvmName("setSecretKey")
    fun secretKey(secretKey: String): Builder {
      Builder.secretKey = secretKey
      return this
    }

    @JvmName("setEndpoint")
    fun endpoint(endpoint: String): Builder {
      Builder.endpoint = endpoint
      return this
    }

    @JvmName("setTimeout")
    fun timeout(timeout: Long): Builder {
      Builder.timeout = timeout
      return this
    }
  }

  private val auth: Auth by dependencies.instance(arg = M(accessKey, secretKey))
  private val omnitradeRepository: OmnitradeRepository by dependencies.instance(arg = M(endpoint, timeout))

  // Public API Calls

  fun getMarkets(): CompletableFuture<List<Market>> {
    return omnitradeRepository.getMarkets()
  }

  fun getOrderBook(market: Market, asksLimit: Int, bidsLimit: Int): CompletableFuture<OrderBook> {
    val options = mutableMapOf(
      "market" to market.id,
      "asks_limit" to asksLimit.toString(),
      "bids_limit" to bidsLimit.toString()
    )

    return omnitradeRepository.getOrderBook(options).withExceptionOnError()
  }

  fun getDepth(market: Market, limit: Int): CompletableFuture<Depth> {
    val options = mutableMapOf(
      "market" to market.id,
      "limit" to limit.toString()
    )

    return omnitradeRepository.getDepth(options).withExceptionOnError()
  }

  fun getOrderMarket(market: Market, marketType: MarketType, volume: Double): CompletableFuture<OrderMarket> {
    val options = mutableMapOf(
      "market" to market.id,
      "type" to marketType.type,
      "volume" to volume.toString()
    )

    return omnitradeRepository.getOrderMarket(options).withExceptionOnError()
  }

  fun getTrades(market: Market, tradeIdSince: Int? = null, tradeIdUntil: Int? = null, timestamp: Long? = null, limit: Int? = null, orderBy: OrderBy): CompletableFuture<List<Trade>> {
    val options = mutableMapOf(
      "market" to market.id,
      "order_by" to orderBy.order
    )

    tradeIdSince?.let { options["since"] = it.toString() }
    tradeIdUntil?.let { options["until"] = it.toString() }
    timestamp?.let { options["timestamp"] = it.toString() }
    limit?.let { options["limit"] = it.toString() }

    return omnitradeRepository.getTrades(options).withExceptionOnError()
  }

  fun getK(market: Market, limit: Int, period: Period, timestamp: Long? = null): CompletableFuture<List<List<Int>>> {
    val options = mutableMapOf(
      "market" to market.id,
      "limit" to limit.toString(),
      "period" to period.time.toString()
    )

    timestamp?.let { options["timestamp"] = it.toString() }

    return omnitradeRepository.getK(options).withExceptionOnError()
  }

  fun getKWithPendingTrades(market: Market, trade: Trade, limit: Int, period: Period): CompletableFuture<KWithPendingTrade> {
    val options = mutableMapOf(
      "market" to market.id,
      "trade" to trade.tid.toString(),
      "limit" to limit.toString(),
      "period" to period.time.toString()
    )

    return omnitradeRepository.getKWithPendingTrades(options).withExceptionOnError()
  }

  // Private API Calls

  fun getMe(): CompletableFuture<Member> {
    checkKeys()

    val options = mutableMapOf<String, String>()
    options["signature"] = this.auth.signedParams(
      HttpMethod.GET.toString(),
      "$API_VERSION/members/me",
      options
    )

    return omnitradeRepository.getMe(options).withExceptionOnError()
  }

  fun getDeposit(txid: String): CompletableFuture<Deposit> {
    checkKeys()

    val options = mutableMapOf("txid" to txid)
    options["signature"] = this.auth.signedParams(
      HttpMethod.GET.toString(),
      "$API_VERSION/deposit",
      options
    )

    return omnitradeRepository.getDeposit(options).withExceptionOnError()
  }

  fun getDeposits(currency: String? = null, limit: Int? = null, state: DepositState? = null): CompletableFuture<List<Deposit>> {
    checkKeys()

    val options = mutableMapOf<String, String>()
    currency?.let { options["currency"] = it }
    limit?.let { options["limit"] = it.toString() }
    state?.let { options["state"] = it.state }

    options["signature"] = this.auth.signedParams(
      HttpMethod.GET.toString(),
      "$API_VERSION/deposits",
      options
    )

    return omnitradeRepository.getDeposits(options).withExceptionOnError()
  }

  fun getDepositAddress(currency: String): CompletableFuture<DepositAddress> {
    checkKeys()

    val options = mutableMapOf("currency" to currency)
    options["signature"] = this.auth.signedParams(
      HttpMethod.GET.toString(),
      "$API_VERSION/deposit_address",
      options
    )

    return omnitradeRepository.getDepositAddress(options).withExceptionOnError()
  }

  fun getOrder(id: Int): CompletableFuture<Order> {
    checkKeys()

    val options = mutableMapOf(
      "id" to id.toString()
    )

    options["signature"] = this.auth.signedParams(
      HttpMethod.GET.toString(),
      "$API_VERSION/order",
      options
    )

    return omnitradeRepository.getOrder(options).withExceptionOnError()
  }

  fun getOrders(market: Market, state: OrderState? = null, limit: Int? = null, page: Int? = null, orderBy: OrderBy? = null): CompletableFuture<List<Order>> {
    checkKeys()

    val options = mutableMapOf("market" to market.id)
    state?.let { options["state"] = it.state }
    limit?.let { options["limit"] = it.toString() }
    page?.let { options["page"] = it.toString() }
    orderBy?.let { options["order_by"] = it.order }

    options["signature"] = this.auth.signedParams(
      HttpMethod.GET.toString(),
      "$API_VERSION/orders",
      options
    )

    return omnitradeRepository.getOrders(options).withExceptionOnError()
  }

  fun getOrders(marketId: String, state: OrderState? = null, limit: Int? = null, page: Int? = null, orderBy: OrderBy? = null): CompletableFuture<List<Order>> {
    checkKeys()

    val options = mutableMapOf("market" to marketId)
    state?.let { options["state"] = it.state }
    limit?.let { options["limit"] = it.toString() }
    page?.let { options["page"] = it.toString() }
    orderBy?.let { options["order_by"] = it.order }

    options["signature"] = this.auth.signedParams(
      HttpMethod.GET.toString(),
      "$API_VERSION/orders",
      options
    )

    return omnitradeRepository.getOrders(options).withExceptionOnError()
  }

  fun createOrder(market: Market, orderSide: OrderSide, volume: Double, price: Double? = null, orderType: OrderType? = null): CompletableFuture<Order> {
    checkKeys()

    val options = mutableMapOf(
      "market" to market.id,
      "side" to orderSide.side,
      "volume" to volume.toString()
    )
    price?.let { options["price"] = it.toString() }
    orderType?.let { options["ord_type"] = it.type }

    options["signature"] = this.auth.signedParams(
      HttpMethod.POST.toString(),
      "$API_VERSION/orders",
      options
    )

    return omnitradeRepository.createOrder(options).withExceptionOnError()
  }

  fun createMultipleOrders(market: Market, ordersSide: List<OrderSide>, ordersVolume: List<Double>, ordersPrice: List<Double>? = null, ordersType: List<OrderType>? = null): CompletableFuture<List<Order>> {
    checkKeys()

    val options = mutableMapOf(
      "market" to market.id,
      "orders[side]" to ordersSide.toQueryStringArray(),
      "orders[volume]" to ordersVolume.toQueryStringArray()
    )
    ordersPrice?.let { options["orders[price]"] = it.toQueryStringArray() }
    ordersType?.let { options["orders[ord_type]"] = it.toQueryStringArray() }

    options["signature"] = this.auth.signedParams(
      HttpMethod.POST.toString(),
      "$API_VERSION/orders/multi",
      options
    )

    return omnitradeRepository.createMultipleOrders(options).withExceptionOnError()
  }

  fun deleteOrder(id: Int): CompletableFuture<Order> {
    checkKeys()

    val options = mutableMapOf(
      "id" to id.toString()
    )

    options["signature"] = this.auth.signedParams(
      HttpMethod.POST.toString(),
      "$API_VERSION/order/delete",
      options
    )

    return omnitradeRepository.deleteOrder(options).withExceptionOnError()
  }

  fun clearAllOrders(market: Market? = null, side: OrderSide? = null): CompletableFuture<List<Order>> {
    checkKeys()

    val options = mutableMapOf<String, String>()
    market?.let { options["market"] = it.id }
    side?.let { options["side"] = it.side }

    options["signature"] = this.auth.signedParams(
      HttpMethod.POST.toString(),
      "$API_VERSION/orders/clear",
      options
    )

    return omnitradeRepository.clearAllOrders(options).withExceptionOnError()
  }

  fun clearBottomOrder(market: Market? = null, side: OrderSide? = null, depth: OrderDepth? = null): CompletableFuture<List<Order>> {
    checkKeys()

    val options = mutableMapOf<String, String>()
    market?.let { options["market"] = it.id }
    side?.let { options["side"] = it.side }
    depth?.let { options["depth"] = it.depth }

    options["signature"] = this.auth.signedParams(
      HttpMethod.POST.toString(),
      "$API_VERSION/orders/clear_bottom",
      options
    )

    return omnitradeRepository.clearBottomOrder(options).withExceptionOnError()
  }

  fun getMyTrades(market: Market, tradeIdSince: Int? = null, tradeIdUntil: Int? = null, timestamp: Long? = null, limit: Int? = null, orderBy: OrderBy): CompletableFuture<List<Trade>> {
    checkKeys()

    val options = mutableMapOf(
      "market" to market.id,
      "order_by" to orderBy.order
    )

    tradeIdSince?.let { options["since"] = it.toString() }
    tradeIdUntil?.let { options["until"] = it.toString() }
    timestamp?.let { options["timestamp"] = it.toString() }
    limit?.let { options["limit"] = it.toString() }

    options["signature"] = this.auth.signedParams(
      HttpMethod.GET.toString(),
      "$API_VERSION/trades/my",
      options
    )

    return omnitradeRepository.getMyTrades(options).withExceptionOnError()
  }

  fun setupKeys(accessKey: String, secretKey: String): OmnitradeClient {
    if (accessKey.isEmpty() || secretKey.isEmpty()) {
      throw OmnitradeNoAuthKeysException()
    }

    this.accessKey = accessKey
    this.secretKey = secretKey
    return this
  }

  private fun checkKeys() {
    if (!auth.hasKeys()) {
      throw OmnitradeNoAuthKeysException()
    }
  }

  private fun <T> CompletableFuture<T>.withExceptionOnError(): CompletableFuture<T> {
    return this.exceptionally { ex ->
      when(ex) {
        is HttpException -> {
          if (ex.code() == HTTP_NOT_FOUND) null
          else throw OmnitradeRequestException(ex.code().toString(), ex.message())
        }
        else -> throw ex
      }
    }
  }
}
