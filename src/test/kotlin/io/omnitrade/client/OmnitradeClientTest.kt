package io.omnitrade.client

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.winterbe.expekt.expect
import com.winterbe.expekt.should
import io.omnitrade.adapters.*
import io.omnitrade.auth.Auth
import io.omnitrade.enumerator.order.OrderDepth
import io.omnitrade.enumerator.order.OrderSide
import io.omnitrade.enumerator.order.OrderState
import io.omnitrade.enumerator.order.OrderType
import io.omnitrade.exception.OmnitradeNoAuthKeysException
import io.omnitrade.fixtures.JsonResponses
import io.omnitrade.model.Account
import io.omnitrade.model.Deposit
import io.omnitrade.model.Market
import io.omnitrade.model.Member
import io.omnitrade.repository.OmnitradeRepository
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.time.Instant
import java.util.*
import kotlin.test.assertFailsWith

object OmnitradeClientTest : Spek({
  describe("OmnitradeClient") {

    val authMock: Auth = mock(Auth::class.java)
    val mockWebServer = MockWebServer()

    val kodeinTest = Kodein {
      bind<Auth>() with factory { _: String, _: String ->
        authMock
      }

      bind<OmnitradeRepository>() with factory { _: String, _: Long ->
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

        val retrofit = Retrofit.Builder()
          .baseUrl(mockWebServer.url("").toString())
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build()

        retrofit.create(OmnitradeRepository::class.java)
      }
    }

    val subject = OmnitradeClient.Builder.build(dependencies = kodeinTest)

    beforeGroup {
      given(authMock.signedParams(
        anyString(),
        anyString(),
        anyMap()))
        .willReturn("signature")
      mockWebServer.start()
    }

    describe("#getMarkets") {
      context("when getting the market list") {
        beforeGroup {
          mockWebServer.enqueue(MockResponse()
            .setBody(JsonResponses.getMarkets())
            .setResponseCode(200)
          )
        }

        it("returns the list of available markets") {
          val markets = listOf(
            Market("btcbrl", "BTC/BRL"),
            Market("ltcbrl", "LTC/BRL")
          )

          val result = subject.getMarkets().get()
          expect(result).to.be.equal(markets)
        }
      }
    }

    describe("#getMe") {
      context("when getting my own information") {
        beforeGroup {
          mockWebServer.enqueue(MockResponse()
            .setBody(JsonResponses.getMe())
            .setResponseCode(200)
          )
        }

        context("with valid credentials") {
          it("returns the list of available markets") {
            given(authMock.hasKeys()).willReturn(true)
            val member = Member(
              sn = "sn_stuff",
              name = "Alcides",
              email = "alcides@test.com",
              activated = true,
              accounts = listOf(
                Account(currency = "btc", balance = 9997.99780467, locked = 2.0),
                Account(currency = "ltc", balance = 10000.0, locked = 0.0)
              )
            )

            expect(subject.getMe().get()).to.be.equal(member)
          }
        }

        context("without credentials") {
          it("throws a OmnitradeNoAuthKeysException") {
            given(authMock.hasKeys()).willReturn(false)
            assertFailsWith<OmnitradeNoAuthKeysException> {
              subject.getMe().get()
            }
          }
        }
      }
    }

    describe("#getDeposits") {
      context("when getting deposits") {
        beforeGroup {
          mockWebServer.enqueue(MockResponse()
            .setBody(JsonResponses.getDeposits())
            .setResponseCode(200)
          )
        }

        context("with valid credentials") {
          it("returns a list of deposits") {
            given(authMock.hasKeys()).willReturn(true)
            val deposits = listOf(
              Deposit(id = 0,
                currency = "brl",
                amount = 0.0,
                fee = 0.0,
                txid = 0.0,
                createdAt = Instant.parse("2019-02-12T15:54:53Z"),
                confirmations = "string",
                doneAt = Instant.parse("2019-02-12T15:54:53Z"),
                state = "accepted"
              ),
              Deposit(id = 1,
                currency = "brl",
                amount = 0.0,
                fee = 0.0,
                txid = 0.0,
                createdAt = Instant.parse("2019-02-12T15:54:53Z"),
                confirmations = "string",
                doneAt = Instant.parse("2019-02-12T15:54:53Z"),
                state = "checked"
              )
            )

            val result = subject.getDeposits().get()

            expect(result).to.be.equal(deposits)
          }
        }

        context("without credentials") {
          it("throws a OmnitradeNoAuthKeysException") {
            given(authMock.hasKeys()).willReturn(false)
            assertFailsWith<OmnitradeNoAuthKeysException> {
              subject.getDeposits().get()
            }
          }
        }
      }
    }

    describe("#getDeposit") {

    }

    describe("#getDepositAddress") {

    }

    describe("#getOrders") {

    }
    describe("#createOrder") {

    }

    describe("#createMultipleOrders") {

    }

    describe("#clearAllOrders") {

    }

    describe("#clearBottomOrder") {

    }

    describe("#getOrder") {

    }

    describe("#deleteOrder") {

    }

    describe("#getOrderBook") {

    }

    describe("#getDepth") {

    }

    describe("#getOrderMarket") {

    }

    describe("#getTrades") {

    }

    describe("#getMyTrades") {

    }

    describe("#getK") {

    }

    describe("#getKWithPendingTrades") {

    }

    describe("#setupKeys") {

    }

    afterGroup {
      mockWebServer.shutdown()
    }
  }
})
