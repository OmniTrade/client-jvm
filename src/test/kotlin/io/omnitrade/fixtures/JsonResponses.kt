package io.omnitrade.fixtures

object JsonResponses {
  fun getMarkets() = """
    [
      {"id":"btcbrl","name":"BTC/BRL"},
      {"id":"ltcbrl","name":"LTC/BRL"}
    ]
  """.trimIndent()

  fun getMe() = """
    {
      "sn":"sn_stuff",
      "name":"Alcides",
      "email":"alcides@test.com",
      "activated":true,
      "accounts":[
        {
          "currency":"btc",
          "balance":"9997.99780467",
          "locked":"2.0"
        },
        {
          "currency":"ltc",
          "balance":"10000.0",
          "locked":"0.0"
        }
      ]
    }
  """.trimIndent()

  fun getDeposits() = """
    [
      {
        "id": 0,
        "currency": "brl",
        "amount": 0,
        "fee": 0,
        "txid": 0,
        "created_at": "2019-02-12T15:54:53Z",
        "confirmations": "string",
        "done_at": "2019-02-12T15:54:53Z",
        "state": "accepted"
      },
      {
        "id": 1,
        "currency": "brl",
        "amount": 0,
        "fee": 0,
        "txid": 0,
        "created_at": "2019-02-12T15:54:53Z",
        "confirmations": "string",
        "done_at": "2019-02-12T15:54:53Z",
        "state": "checked"
      }
    ]
  """.trimIndent()
}
